package br.com.nafer.gerenciadorcripto.domain.model.dtos

import br.com.nafer.gerenciadorcripto.domain.model.enums.TipoOperacaoEnum
import java.math.BigDecimal
import java.time.LocalDateTime

data class OperacoesDTO(
    val finalidade: FinalidadesDTO,
    val dataOperacaoEntrada: LocalDateTime?,
    val moedaEntrada: MoedaDTO?,
    val quantidadeEntrada: BigDecimal?,
    val dataOperacaoSaida: LocalDateTime?,
    val moedaSaida: MoedaDTO?,
    val quantidadeSaida: BigDecimal?,
    val valorBrl: BigDecimal? = null,
    val lucroPrejuizo: BigDecimal? = null,
    val destino: String? = null,
    val wallet: String? = null,
    val taxaQuantidade: BigDecimal? = null,
    val taxaMoeda: String? = null,
    val taxaValorBrl: BigDecimal? = null,
    val dataCriacao: LocalDateTime = LocalDateTime.now(),
    val excluido: Boolean = false,
    val origemRegistro: String,
    val observacao: String? = null,
    val tipoOperacao: TipoOperacaoEnum
)
