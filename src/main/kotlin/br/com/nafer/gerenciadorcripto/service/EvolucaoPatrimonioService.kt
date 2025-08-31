package br.com.nafer.gerenciadorcripto.service

import br.com.nafer.gerenciadorcripto.domain.model.dtos.EvolucaoPatrimonioDTO
import br.com.nafer.gerenciadorcripto.domain.model.dtos.PontoGraficoPatrimonio
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class EvolucaoPatrimonioService {

    /**
     * Gera dados mockados de evolução do patrimônio similares ao exemplo do Investidor 10
     * Simula um período de 13 meses com investimentos mensais e variações de ganho/perda
     */
    fun obterDadosMockados(): List<EvolucaoPatrimonioDTO> {
        val dataInicial = LocalDate.of(2024, 8, 1)
        val dados = mutableListOf<EvolucaoPatrimonioDTO>()
        
        // Valores base similares ao gráfico do exemplo
        val investimentosMensais = listOf(
            BigDecimal("50000"),   // 08/24
            BigDecimal("80000"),   // 09/24
            BigDecimal("100000"),  // 10/24
            BigDecimal("180000"),  // 11/24
            BigDecimal("200000"),  // 12/24
            BigDecimal("230000"),  // 01/25
            BigDecimal("250000"),  // 02/25
            BigDecimal("270000"),  // 03/25
            BigDecimal("260000"),  // 04/25 (pequena redução)
            BigDecimal("240000"),  // 05/25 (redução)
            BigDecimal("230000"),  // 06/25
            BigDecimal("250000"),  // 07/25
            BigDecimal("280000")   // 08/25
        )
        
        // Ganhos/perdas de capital (valores que variam para simular volatilidade)
        val ganhosCapital = listOf(
            BigDecimal("5000"),    // 08/24
            BigDecimal("8000"),    // 09/24
            BigDecimal("12000"),   // 10/24
            BigDecimal("95000"),   // 11/24 (grande alta)
            BigDecimal("85000"),   // 12/24
            BigDecimal("70000"),   // 01/25
            BigDecimal("45000"),   // 02/25
            BigDecimal("-30000"),  // 03/25 (perda)
            BigDecimal("-35000"),  // 04/25
            BigDecimal("-25000"),  // 05/25
            BigDecimal("-15000"),  // 06/25
            BigDecimal("20000"),   // 07/25 (recuperação)
            BigDecimal("35000")    // 08/25
        )
        
        investimentosMensais.forEachIndexed { index, valorInvestido ->
            val data = dataInicial.plusMonths(index.toLong())
            val ganhoCapital = ganhosCapital[index]
            val valorTotal = valorInvestido + ganhoCapital
            
            dados.add(
                EvolucaoPatrimonioDTO(
                    data = data,
                    valorInvestido = valorInvestido,
                    ganhoCapital = ganhoCapital,
                    valorTotal = valorTotal
                )
            )
        }
        
        return dados
    }
    
    /**
     * Converte os dados de evolução para pontos do gráfico
     */
    fun converterParaPontosGrafico(dados: List<EvolucaoPatrimonioDTO>): List<PontoGraficoPatrimonio> {
        return dados.map { dto ->
            PontoGraficoPatrimonio(
                label = dto.formatarDataParaGrafico(),
                valorInvestido = dto.valorInvestido.toFloat(),
                ganhoCapital = dto.ganhoCapital.toFloat()
            )
        }
    }
}