package br.com.nafer.gerenciadorcripto.domain.model.dtos

import java.math.BigDecimal
import java.time.LocalDate

/**
 * DTO para representar dados de evolução do patrimônio
 * @param data Data do registro
 * @param valorInvestido Valor total investido até aquela data
 * @param ganhoCapital Ganho de capital até aquela data
 * @param valorTotal Valor total do patrimônio (investido + ganho)
 */
data class EvolucaoPatrimonioDTO(
    val data: LocalDate,
    val valorInvestido: BigDecimal,
    val ganhoCapital: BigDecimal,
    val valorTotal: BigDecimal
) {
    /**
     * Formata a data no formato MM/yy para exibição no gráfico
     */
    fun formatarDataParaGrafico(): String {
        val mes = String.format("%02d", data.monthValue)
        val ano = String.format("%02d", data.year % 100)
        return "$mes/$ano"
    }
}

/**
 * Ponto de dados para o gráfico de evolução do patrimônio
 * @param label Rótulo do eixo X (formato MM/yy)
 * @param valorInvestido Valor investido
 * @param ganhoCapital Ganho de capital
 */
data class PontoGraficoPatrimonio(
    val label: String,
    val valorInvestido: Float,
    val ganhoCapital: Float
) {
    val valorTotal: Float
        get() = valorInvestido + ganhoCapital
}