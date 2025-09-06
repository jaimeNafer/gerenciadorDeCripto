package br.com.nafer.gerenciadorcripto.controllers.dtos

import br.com.nafer.gerenciadorcripto.domain.model.enums.StatusOperacaoEnum
import br.com.nafer.gerenciadorcripto.domain.model.enums.TipoOperacaoEnum
import java.math.BigDecimal
import java.time.LocalDateTime

data class OperacoesResponse(
    val idOperacao: Int,
    val arquivo: ArquivoResponse,
    val finalidade: FinalidadesResponse,
    val dataOperacaoEntrada: LocalDateTime?,
    val moedaEntrada: MoedaResponse?,
    val quantidadeEntrada: BigDecimal?,
    val dataOperacaoSaida: LocalDateTime?,
    val moedaSaida: MoedaResponse?,
    val quantidadeSaida: BigDecimal?,
    val valorBrl: BigDecimal? = null,
    val lucroPrejuizo: BigDecimal? = null,
    val destino: String? = null,
    val taxaQuantidade: BigDecimal? = null,
    val taxaMoeda: String? = null,
    val taxaValorBrl: BigDecimal? = null,
    val dataCriacao: LocalDateTime = LocalDateTime.now(),
    val excluido: Boolean = false,
    val origemRegistro: String,
    val observacao: String? = null,
    val tipoOperacao: TipoOperacaoEnum,
    val statusOperacao: StatusOperacaoEnum
)
