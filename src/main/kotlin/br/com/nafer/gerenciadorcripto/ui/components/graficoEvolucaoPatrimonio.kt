package br.com.nafer.gerenciadorcripto.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.nafer.gerenciadorcripto.ui.screens.HistoricoMensalDTO
import com.netguru.multiplatform.charts.bar.BarChart
import com.netguru.multiplatform.charts.bar.BarChartCategory
import com.netguru.multiplatform.charts.bar.BarChartData
import com.netguru.multiplatform.charts.bar.BarChartEntry

@Composable
fun graficoEvolucaoPatrimonioChart(data: List<HistoricoMensalDTO>) {
    val entries = data.map {
        val investido = it.valorTotal.toFloat() * 0.75f
        val ganho = it.valorTotal.toFloat() * 0.25f

        BarChartEntry(
            x = it.mes,
            y = investido + ganho,
            color = Color(0xFF66BB6A) // cor combinada
        )
    }

    val chartData = BarChartData(
        categories = listOf(
            BarChartCategory(
                name = "Patrimônio Mensal",
                entries = entries
            )
        ),
    )

    BarChart(
        data = chartData,
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(16.dp)
    )
}

