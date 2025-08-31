package br.com.nafer.gerenciadorcripto.service

import br.com.nafer.gerenciadorcripto.clients.CoingeckoClient
import br.com.nafer.gerenciadorcripto.domain.mappers.OperacaoMapper
import br.com.nafer.gerenciadorcripto.domain.model.Arquivo
import br.com.nafer.gerenciadorcripto.domain.model.Carteira
import br.com.nafer.gerenciadorcripto.domain.model.Corretora
import br.com.nafer.gerenciadorcripto.domain.model.Operacoes
import br.com.nafer.gerenciadorcripto.domain.model.Usuario
import br.com.nafer.gerenciadorcripto.domain.model.dtos.RegistroEmParesDTO
import br.com.nafer.gerenciadorcripto.domain.model.enums.FinalidadeOperacaoEnum
import br.com.nafer.gerenciadorcripto.domain.model.enums.TipoOperacaoEnum
import br.com.nafer.gerenciadorcripto.dtos.binance.ArquivoDTO
import br.com.nafer.gerenciadorcripto.dtos.binance.CsvBinanceDTO
import br.com.nafer.gerenciadorcripto.infrastructure.repository.CarteiraRepository
import br.com.nafer.gerenciadorcripto.infrastructure.repository.CorretoraRepository
import br.com.nafer.gerenciadorcripto.infrastructure.repository.OperacaoRepository
import br.com.nafer.gerenciadorcripto.infrastructure.repository.UsuarioRepository
import br.com.nafer.gerenciadorcripto.utils.csvUtils.carregar
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class ImportacaoService(
    private val arquivoService: ArquivoService,
    private val operacaoRepository: OperacaoRepository,
    private val finalidadesService: FinalidadesService,
    private val corretoraRepository: CorretoraRepository,
    private val usuarioRepository: UsuarioRepository,
    private val carteiraRepository: CarteiraRepository,
    private val moedaService: MoedaService,
    private val coingeckoClient: CoingeckoClient,
    private val carteiraService: CarteiraService,
    private val mapper: OperacaoMapper,
) {

    fun processarImportacao(
        arquivoDTO: ArquivoDTO,
        idCorretora: Int,
        idUsuario: Int
    ) {
        val usuario = usuarioRepository.findById(idUsuario).orElseThrow { RuntimeException("Usuario não encontrado") }
        val corretora = corretoraRepository.findById(idCorretora).orElseThrow { RuntimeException("Corretora não encontrada") }
        val carteira = carteiraRepository.filtrarCarteiraPorUsuarioECorretora(usuario, corretora)
            ?: carteiraRepository.save(Carteira(usuario = usuario, corretora = corretora))
        val arquivo = arquivoService.salvar(usuario, corretora, arquivoDTO)

        val listaDeRegistros = carregar(arquivoDTO, CsvBinanceDTO::class.java)

        val operacoes = obterOperacoes(
            listaDeRegistros,
            carteira,
            arquivo
        )
        operacaoRepository.saveAll(operacoes)
    }

    @Transactional
    fun processarExclusaoArquivo(arquivoDTO: ArquivoDTO) {
        val arquivoExistente = arquivoService.obter(arquivoDTO)
        operacaoRepository.deleteAllByArquivo(arquivoExistente)
        arquivoService.deletar(arquivoExistente)
    }

    private fun obterOperacoes(
        listaDeRegistros: List<CsvBinanceDTO>,
        carteira: Carteira,
        arquivo: Arquivo,
    ): List<Operacoes> {

        val operacoes = FinalidadeOperacaoEnum.values()
            .flatMap { finalidade -> filtrarOperacaoPorFinalidade(listaDeRegistros, carteira.corretora, finalidade) }
        return operacoes.map { registroEmPares ->
            val finalidade = finalidadesService.obterFinalidadePorCorretoraENome(carteira.corretora, registroEmPares.finalidade)
            val moedaEntrada =
                registroEmPares.entrada?.let { regEntrada -> moedaService.obterMoedaPorTicker(regEntrada.coin) }
            val moedaSaida = registroEmPares.saida?.let { regSaida -> moedaService.obterMoedaPorTicker(regSaida.coin) }
            Operacoes(
                idOperacao = null,
                carteira = carteira,
                finalidade = finalidade,
                arquivo = arquivo,
                dataOperacaoEntrada = registroEmPares.entrada?.utcTime,
                moedaEntrada = moedaEntrada,
                quantidadeEntrada = registroEmPares.entrada?.change,
                dataOperacaoSaida = registroEmPares.saida?.utcTime,
                moedaSaida = moedaSaida,
                quantidadeSaida = registroEmPares.saida?.change,
                valorBrl = BigDecimal.ZERO,
                lucroPrejuizo = BigDecimal.ZERO,
                destino = null,
                taxaQuantidade = null,
                taxaMoeda = null,
                taxaValorBrl = null,
                dataCriacao = LocalDateTime.now(),
                excluido = false,
                origemRegistro = "ARQUIVO",
                observacao = null,
                tipoOperacao = registroEmPares.tipoOperacao
            )
        }
    }

    private fun filtrarOperacaoPorFinalidade(
        registros: List<CsvBinanceDTO>,
        corretora: Corretora,
        finalidade: FinalidadeOperacaoEnum
    ): List<RegistroEmParesDTO> {
        val registrosFiltradosPorfinalidade = obterRegistrosPorFinalidade(registros, corretora, finalidade)
        return agruparRegistrosEmPares(registrosFiltradosPorfinalidade, finalidade)
    }

    private fun obterRegistrosPorFinalidade(
        registros: List<CsvBinanceDTO>,
        corretora: Corretora,
        finalidade: FinalidadeOperacaoEnum
    ): List<CsvBinanceDTO> {
        val codigoExchange = finalidadesService.obterFinalidadePorCorretoraENome(corretora, finalidade).codigoExchange
        return registros.stream()
            .filter { op ->
                op.operation.lowercase() == codigoExchange.lowercase()
            }
            .toList()
    }

    private fun agruparRegistrosEmPares(
        registros: List<CsvBinanceDTO>,
        finalidade: FinalidadeOperacaoEnum
    ): List<RegistroEmParesDTO> {
        return if (ehFinalidadeConversaoOuCompraFiat(finalidade) || ehFinalidadeTransferencia(finalidade)) {
            agruparRegistrosDeFinalidadeConversaoCompraFiatTransferencia(registros, finalidade)
        } else {
            agruparOperacoes(registros, finalidade)
        }
    }

    private fun agruparRegistrosDeFinalidadeConversaoCompraFiatTransferencia(
        registros: List<CsvBinanceDTO>,
        finalidade: FinalidadeOperacaoEnum
    ): List<RegistroEmParesDTO> {
        val registrosEmPares: MutableList<RegistroEmParesDTO> = ArrayList<RegistroEmParesDTO>()
        var i = 0
        val registrosOrdenadosPorData = registros.sortedBy { it.utcTime }
        while (i < registrosOrdenadosPorData.size) {
            val registroEntrada =
                if (valorEhPositivo(registrosOrdenadosPorData[i].change)) registrosOrdenadosPorData[i] else registrosOrdenadosPorData[i + 1]
            val registroSaida =
                if (!valorEhPositivo(registrosOrdenadosPorData[i].change)) registrosOrdenadosPorData[i] else registrosOrdenadosPorData[i + 1]
            val tipoOperacao = obterTipoOperacao(registroEntrada, registroSaida, finalidade)
            registrosEmPares.add(RegistroEmParesDTO(registroEntrada, registroSaida, finalidade, tipoOperacao))
            i += 2
        }
        return registrosEmPares
    }

    private fun agruparOperacoes(
        registros: List<CsvBinanceDTO>,
        finalidade: FinalidadeOperacaoEnum
    ): List<RegistroEmParesDTO> {
        return registros.map { registro ->
            val registroEntrada = if (valorEhPositivo(registro.change)) registro else null
            val registroSaida = if (!valorEhPositivo(registro.change)) registro else null
            val tipoOperacao = obterTipoOperacao(registroEntrada, registroSaida, finalidade)
            RegistroEmParesDTO(registroEntrada, registroSaida, finalidade, tipoOperacao)
        }
    }

    private fun obterTipoOperacao(
        entrada: CsvBinanceDTO?,
        saida: CsvBinanceDTO?,
        finalidade: FinalidadeOperacaoEnum
    ): TipoOperacaoEnum {
        return when {
            ehFinalidadeConversaoOuCompraFiat(finalidade) && entrada?.coin?.uppercase() != "BRL" && saida?.coin?.uppercase() != "BRL" -> TipoOperacaoEnum.PERMUTA
            ehFinalidadeConversaoOuCompraFiat(finalidade) && entrada?.coin?.uppercase() == "BRL" && saida?.coin?.uppercase() != "BRL" -> TipoOperacaoEnum.VENDA
            ehFinalidadeConversaoOuCompraFiat(finalidade) && entrada?.coin?.uppercase() != "BRL" && saida?.coin?.uppercase() == "BRL" -> TipoOperacaoEnum.COMPRA
            else -> TipoOperacaoEnum.OUTROS
        }
    }

    private fun valorEhPositivo(valor: BigDecimal): Boolean {
        return valor >= BigDecimal.ZERO
    }

    private fun ehFinalidadeConversaoOuCompraFiat(finalidade: FinalidadeOperacaoEnum): Boolean {
        return finalidade == FinalidadeOperacaoEnum.CONVERSAO || finalidade == FinalidadeOperacaoEnum.COMPRA_FIAT
    }

    private fun ehFinalidadeTransferencia(finalidade: FinalidadeOperacaoEnum): Boolean {
        return finalidade == FinalidadeOperacaoEnum.TRANSFERENCIA
    }
}
