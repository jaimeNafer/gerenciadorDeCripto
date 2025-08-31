package br.com.nafer.gerenciadorcripto.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.math.BigDecimal


@Composable
fun ResumoCarteira(
    valorAtual: BigDecimal,
    valorInvestido: BigDecimal,
    lucroPrejuizo: BigDecimal,
    rentabilidadePercentual: BigDecimal
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            CardResumo("Patrimônio atual", valorAtual, Color(0xFF1976D2), rentabilidadePercentual)
        }
        Box(modifier = Modifier.weight(1f)) {
            CardResumo("Valor investido", valorInvestido, Color(0xFF0288D1), rentabilidadePercentual)
        }
        Box(modifier = Modifier.weight(1f)) {
            CardResumo("Rentabilidade", BigDecimal.ZERO, Color(0xFF388E3C), rentabilidadePercentual)
        }
        Box(modifier = Modifier.weight(1f)) {
            CardResumo("Lucro/Prejuízo", lucroPrejuizo, colorFromValue(lucroPrejuizo), rentabilidadePercentual)
        }
    }
}

@Composable
fun CardResumo(
    titulo: String,
    valor: BigDecimal,
    backgroundColor: Color,
    percentual: BigDecimal? = null
) {
    Card(
        elevation = 4.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(titulo, style = MaterialTheme.typography.caption, color = Color.Gray)
            Text(
                text = Formatador.formatarMoeda(valor),
                style = MaterialTheme.typography.h6,
                color = backgroundColor,
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            )
            percentual?.let {
                val sinal = if (it >= BigDecimal.ZERO) "+" else ""
                val cor = if (it >= BigDecimal.ZERO) Color(0xFF2E7D32) else Color.Red
                Text("$sinal${it.setScale(2)}%", color = cor)
            }
        }
    }
}

fun colorFromValue(valor: BigDecimal): Color {
    return when {
        valor > BigDecimal.ZERO -> Color(0xFF2E7D32)
        valor < BigDecimal.ZERO -> Color.Red
        else -> Color.Gray
    }
}