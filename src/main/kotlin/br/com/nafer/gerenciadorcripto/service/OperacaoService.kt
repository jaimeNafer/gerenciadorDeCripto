package br.com.nafer.gerenciadorcripto.service

import br.com.nafer.gerenciadorcripto.clients.BinanceApiClient
import br.com.nafer.gerenciadorcripto.clients.CoingeckoClient
import br.com.nafer.gerenciadorcripto.domain.mappers.OperacaoMapper
import br.com.nafer.gerenciadorcripto.domain.model.Corretora
import br.com.nafer.gerenciadorcripto.domain.model.Operacoes
import br.com.nafer.gerenciadorcripto.domain.model.Usuario
import br.com.nafer.gerenciadorcripto.domain.model.dtos.OperacoesDTO
import br.com.nafer.gerenciadorcripto.domain.model.enums.StatusOperacaoEnum
import br.com.nafer.gerenciadorcripto.domain.model.enums.TipoOperacaoEnum
import br.com.nafer.gerenciadorcripto.infrastructure.repository.CorretoraRepository
import br.com.nafer.gerenciadorcripto.infrastructure.repository.OperacaoRepository
import br.com.nafer.gerenciadorcripto.infrastructure.repository.UsuarioRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

@Service
class OperacaoService(
    private val arquivoService: ArquivoService,
    private val operacaoRepository: OperacaoRepository,
    private val finalidadesService: FinalidadesService,
    private val corretoraRepository: CorretoraRepository,
    private val usuarioRepository: UsuarioRepository,
    private val moedaService: MoedaService,
    private val coingeckoClient: CoingeckoClient,
    private val binanceApiClient: BinanceApiClient,
    private val carteiraService: CarteiraService,
    private val mapper: OperacaoMapper,
) {

    fun listarOperacoes(): List<OperacoesDTO> {
        processarOperacoesPendentes()
        return operacaoRepository.findAll().map { mapper.toDTO(it) }
    }

    private fun processarOperacoesPendentes() {
        val operacoesComStatusPendente = operacaoRepository.findAllByStatusOperacao(StatusOperacaoEnum.PENDENTE)
        val paresDistintos = operacoesComStatusPendente.map { Pair(it.usuario, it.corretora) }
            .distinct()
            paresDistintos.forEach { pair ->
                val usuario = pair.first
                val corretora = pair.second
                val operacoesPendentes = operacaoRepository.findAllByUsuarioAndCorretoraAndTipoOperacaoIn(usuario, corretora, listOf(TipoOperacaoEnum.VENDA, TipoOperacaoEnum.COMPRA, TipoOperacaoEnum.PERMUTA))
                val listaDeTickersPendenteAjuste = obterTickersComOperacoesPendentes(operacoesPendentes)
                listaDeTickersPendenteAjuste.forEach { ticker ->
                    val operacoesPendente = filtrarOperacoesPorTickerDeEntradaESaida(operacoesPendentes, ticker)
                    calcularPrecoMedioELucroPrejuizo(usuario, corretora, operacoesPendente, ticker)
                }
            }
    }

    private fun calcularPrecoMedioELucroPrejuizo(
        usuario: Usuario,
        corretora: Corretora,
        operacoes: List<Operacoes>,
        ticker: String
    ) {
        var quantidadeTotal = BigDecimal.ZERO
        var precoMedioTotal = BigDecimal.ZERO
        var valorTotal = BigDecimal.ZERO
        var lucroPrejuizoTotal = BigDecimal.ZERO
        var status: StatusOperacaoEnum
        operacoes.sortedBy { it.dataOperacaoEntrada }.forEach { operacao ->
            try {
                when (operacao.tipoOperacao) {
                    TipoOperacaoEnum.COMPRA -> {
                        quantidadeTotal = quantidadeTotal.add(operacao.quantidadeEntrada)
                        val valor = valorTotal.add(operacao.quantidadeSaida?.abs())
                        precoMedioTotal = valor.divide(quantidadeTotal, 2, RoundingMode.HALF_EVEN)
                        valorTotal = quantidadeTotal.multiply(precoMedioTotal)

                    }

                    TipoOperacaoEnum.VENDA -> {
                        val precoMedioOperacao =
                            operacao.quantidadeEntrada?.divide(operacao.quantidadeSaida, 2, RoundingMode.HALF_EVEN)?.abs()
                        quantidadeTotal = quantidadeTotal.minus(operacao.quantidadeSaida?.abs()!!)
                        valorTotal = quantidadeTotal.multiply(precoMedioTotal)
                        val lucroPrejuizoDaOperacao = operacao.quantidadeSaida.abs().multiply(precoMedioOperacao?.minus(precoMedioTotal))
                        lucroPrejuizoTotal = lucroPrejuizoTotal.add(lucroPrejuizoDaOperacao)
                        operacao.lucroPrejuizo = lucroPrejuizoDaOperacao
                    }

                    TipoOperacaoEnum.PERMUTA -> {
                        val ehPermutaDeCompra = operacao.moedaEntrada?.ticker == ticker
                        if (ehPermutaDeCompra) {
                            val valorDaMoedaEmBrl = binanceApiClient.buscarPrecoDaMoedaPorTicker(
                                operacao.moedaSaida?.ticker!!,
                                operacao.dataOperacaoEntrada!!
                            ).precoBrl
                            val valorBrl = operacao.quantidadeSaida?.multiply(valorDaMoedaEmBrl)?.abs()
                            operacao.valorBrl = valorBrl
                            quantidadeTotal = quantidadeTotal.add(operacao.quantidadeEntrada)
                            val valor = valorTotal.add(valorBrl)
                            precoMedioTotal = valor.divide(quantidadeTotal, 2, RoundingMode.HALF_EVEN)
                            valorTotal = quantidadeTotal.multiply(precoMedioTotal)
                        } else {
                            val valorDaMoedaEmBrl = binanceApiClient.buscarPrecoDaMoedaPorTicker(
                                operacao.moedaEntrada?.ticker!!,
                                operacao.dataOperacaoEntrada!!
                            ).precoBrl
                            val valorBrl = operacao.quantidadeEntrada?.multiply(valorDaMoedaEmBrl)
                            val precoMedioOperacao =
                                valorBrl?.divide(operacao.quantidadeSaida, 2, RoundingMode.HALF_EVEN)?.abs()
                            quantidadeTotal = quantidadeTotal.minus(operacao.quantidadeSaida?.abs()!!)
                            valorTotal = quantidadeTotal.multiply(precoMedioTotal)
                            val lucroPrejuizoDaOperacao = operacao.quantidadeSaida.abs().multiply(precoMedioOperacao?.minus(precoMedioTotal))
                            lucroPrejuizoTotal = lucroPrejuizoTotal.add(lucroPrejuizoDaOperacao)
                            operacao.lucroPrejuizo = lucroPrejuizoDaOperacao
                            operacao.valorBrl = valorBrl
                        }
                    }
                    else -> {}
                }
                status = StatusOperacaoEnum.PROCESSADA
            }catch (ex:  Exception){
                status = StatusOperacaoEnum.ERRO
            }
            operacao.statusOperacao = status
        }
        operacaoRepository.saveAll(operacoes)
        carteiraService.atualizarAtivo(usuario, corretora, ticker, quantidadeTotal, valorTotal, precoMedioTotal, lucroPrejuizoTotal)
    }

    private fun obterTickersComOperacoesPendentes(operacoes: List<Operacoes>): List<String> {
        val moedasDeEntradaComStatusPendente = operacoes
            .filter { it.statusOperacao == StatusOperacaoEnum.PENDENTE && it.moedaEntrada?.fiat == false }
            .mapNotNull { it.moedaEntrada?.ticker }
        val moedasDeSaidaComStatusPendente = operacoes
            .filter { it.statusOperacao == StatusOperacaoEnum.PENDENTE && it.moedaSaida?.fiat == false }
            .mapNotNull { it.moedaSaida?.ticker }
        return (moedasDeEntradaComStatusPendente + moedasDeSaidaComStatusPendente).distinct()
    }

    private fun filtrarOperacoesPorTickerDeEntradaESaida(operacoes: List<Operacoes>, ticker: String): List<Operacoes> {
        return operacoes.filter { it.moedaEntrada?.ticker == ticker || it.moedaSaida?.ticker == ticker }
    }

    private fun obterHistoricoValorBrl(data: LocalDateTime?): BigDecimal? {
        return BigDecimal.ZERO
    }
}
