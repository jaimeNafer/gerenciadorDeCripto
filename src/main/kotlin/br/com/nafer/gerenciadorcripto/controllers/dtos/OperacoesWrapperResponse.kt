package br.com.nafer.gerenciadorcripto.controllers.dtos

import java.math.BigDecimal

data class OperacoesWrapperResponse(
    val mesAnoReferencia: String,
    val consolidado: ConsolidadoMensal,
    val operacoes: List<OperacoesResponse>
)

data class ConsolidadoMensal(
    val numeroTotalMovimentacoes: Int,
    val valorTotalMovimentacoes: BigDecimal,
    val valorTotalCompras: BigDecimal,
    val valorTotalVendas: BigDecimal,
    val valorTotalPermutas: BigDecimal,
    val valorTotalLucroPrejuizo: BigDecimal,
    val deveDeclararIN1888: Boolean
)
