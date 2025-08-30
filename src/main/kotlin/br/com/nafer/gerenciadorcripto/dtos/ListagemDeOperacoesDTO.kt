package br.com.nafer.gerenciadorcripto.dtos

import br.com.nafer.gerenciadorcripto.domain.model.enums.TipoOperacaoEnum
import java.math.BigDecimal
import java.time.LocalDateTime

data class ListagemDeOperacoesDTO(
    val dataOperacao: LocalDateTime,
    val ativoEntrada: String?,
    val iconeAtivoEntrada: String?,
    val quantidadeEntrada: BigDecimal?,
    val ativoSaida: String?,
    val iconeAtivoSaida: String?,
    val quantidadeSaida: BigDecimal?,
    val valorBrl: BigDecimal? = null,
    val lucroPrejuizo: BigDecimal? = null,
    val finalidade: String?,
    val tipoOperacao: TipoOperacaoEnum
)
