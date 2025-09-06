package br.com.nafer.gerenciadorcripto.services

import br.com.nafer.gerenciadorcripto.clients.BinanceApiClient
import br.com.nafer.gerenciadorcripto.controllers.dtos.OperacoesResponse
import br.com.nafer.gerenciadorcripto.domain.mappers.OperacaoMapper
import br.com.nafer.gerenciadorcripto.domain.model.Arquivo
import br.com.nafer.gerenciadorcripto.domain.model.Carteira
import br.com.nafer.gerenciadorcripto.domain.model.Operacoes
import br.com.nafer.gerenciadorcripto.domain.model.enums.StatusOperacaoEnum
import br.com.nafer.gerenciadorcripto.domain.model.enums.TipoOperacaoEnum
import br.com.nafer.gerenciadorcripto.exceptions.UnprocessableEntityException
import br.com.nafer.gerenciadorcripto.infrastructure.repository.OperacaoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

@Service
class OperacaoService(
    private val operacaoRepository: OperacaoRepository,
    private val binanceApiClient: BinanceApiClient,
    private val carteiraService: CarteiraService,
    private val importacaoService: ImportacaoService,
    private val mapper: OperacaoMapper,
) {
    fun criarOperacoesPorArquivo(arquivo: Arquivo, file: MultipartFile) {
        val operacoes = importacaoService.converterCsvEmOperacoes(arquivo, file)
        operacaoRepository.saveAll(operacoes)
        processarOperacoesPendentes(arquivo)
    }
    @Transactional
    fun removerTodasOperacoesPorArquivo(arquivo: Arquivo) {
        if (arquivo.idArquivo != null && arquivo.carteira.idCarteira != null) {
            val tickersEntrada = operacaoRepository.obterMoedaEntradaTickersDistintosPorIdArquivo(arquivo.idArquivo)
            val tickersSaida = operacaoRepository.obterMoedaSaidaTickersDistintosPorIdArquivo(arquivo.idArquivo)
            val tickersAfetados = (tickersEntrada + tickersSaida).distinct()
            operacaoRepository.atualizarStatusPorCarteiraETickers(
                arquivo.carteira.idCarteira,
                tickersAfetados,
                StatusOperacaoEnum.REPROCESSAR
            )
            operacaoRepository.deleteAllByArquivoIdArquivo(arquivo.idArquivo)
            carteiraService.removerAtivos(arquivo.carteira, tickersAfetados)
            processarOperacoesPendentes(arquivo)
        } else {
            throw UnprocessableEntityException("Arquivo e Carteira não podem ter ids nulos")
        }
    }
    fun listarOperacoes(idCarteira: Int): List<OperacoesResponse> {
        return operacaoRepository.findAllByArquivoCarteiraIdCarteira(idCarteira).map { mapper.toResponse(it) }
    }
    private fun processarOperacoesPendentes(arquivo: Arquivo) {
        val operacoes = operacaoRepository.findAllByStatusOperacaoNotAndArquivoIdArquivoAndTipoOperacaoIn(
            StatusOperacaoEnum.PROCESSADA,
            arquivo.idArquivo!!,
            listOf(TipoOperacaoEnum.COMPRA, TipoOperacaoEnum.VENDA, TipoOperacaoEnum.PERMUTA)
        )
        val listaDeTickersPendenteAjuste = obterTickersDeOperacaoNaoProcessadas(operacoes)
        listaDeTickersPendenteAjuste.forEach { ticker ->
            val operacoesPendente = filtrarOperacoesPorTickerDeEntradaESaida(operacoes, ticker)
            calcularPrecoMedioELucroPrejuizo(arquivo.carteira, operacoesPendente, ticker)
        }
    }
    @Transactional
    private fun calcularPrecoMedioELucroPrejuizo(
        carteira: Carteira,
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
        carteiraService.atualizarAtivo(carteira, ticker, quantidadeTotal, valorTotal, precoMedioTotal, lucroPrejuizoTotal)
    }

    private fun obterTickersDeOperacaoNaoProcessadas(operacoes: List<Operacoes>): List<String> {
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
