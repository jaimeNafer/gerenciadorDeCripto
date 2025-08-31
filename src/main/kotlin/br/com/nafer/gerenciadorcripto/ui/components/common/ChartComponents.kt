package br.com.nafer.gerenciadorcripto.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.nafer.gerenciadorcripto.domain.model.dtos.PontoGraficoPatrimonio
import java.text.NumberFormat
import java.util.*

/**
 * Componente de gráfico de evolução do patrimônio
 * Exibe um gráfico de barras empilhadas mostrando valor investido e ganho de capital
 * @param titulo Título do gráfico
 * @param dados Lista de pontos de dados do gráfico
 * @param modifier Modificador para customização
 */
@Composable
fun GraficoEvolucaoPatrimonio(
    titulo: String,
    dados: List<PontoGraficoPatrimonio>,
    modifier: Modifier = Modifier
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Título do gráfico
            Text(
                text = titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Legenda
            Row(
                modifier = Modifier.padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                LegendaItem(
                    cor = Color(0xFF4CAF50),
                    texto = "Valor aplicado"
                )
                Spacer(modifier = Modifier.width(12.dp))
                LegendaItem(
                    cor = Color(0xFF81C784),
                    texto = "Ganho capital"
                )
            }
            
            // Área do gráfico com eixos
            if (dados.isNotEmpty()) {
                val valorMaximo = dados.maxOf { it.valorTotal }
                val alturaGrafico = 200.dp
                
                Column {
                    // Container com eixo Y e gráfico
                    Row {
                        // Eixo Y (valores)
                        Column(
                            modifier = Modifier.width(60.dp).height(alturaGrafico),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            val steps = 5
                            for (i in steps downTo 0) {
                                val value = (valorMaximo * i / steps).toInt()
                                Text(
                                    text = if (value >= 1000) "${value/1000}k" else "$value",
                                    fontSize = 10.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Área do gráfico
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(alturaGrafico),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                dados.forEach { ponto ->
                                    BarraGrafico(
                                        ponto = ponto,
                                        alturaMaxima = alturaGrafico,
                                        valorMaximo = valorMaximo,
                                        formatter = formatter
                                    )
                                }
                            }
                            
                            // Eixo X (datas)
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                dados.forEach { ponto ->
                                    Text(
                                        text = ponto.label,
                                        fontSize = 9.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhum dado disponível",
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun LegendaItem(
    cor: Color,
    texto: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(cor, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = texto,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun BarraGrafico(
    ponto: PontoGraficoPatrimonio,
    alturaMaxima: androidx.compose.ui.unit.Dp,
    valorMaximo: Float,
    formatter: NumberFormat
) {
    val alturaInvestido = if (valorMaximo > 0) {
        (ponto.valorInvestido / valorMaximo * alturaMaxima.value).dp
    } else 0.dp
    
    val alturaGanho = if (valorMaximo > 0 && ponto.ganhoCapital > 0) {
        (ponto.ganhoCapital / valorMaximo * alturaMaxima.value).dp
    } else 0.dp
    
    val alturaPerda = if (valorMaximo > 0 && ponto.ganhoCapital < 0) {
        (kotlin.math.abs(ponto.ganhoCapital) / valorMaximo * alturaMaxima.value).dp
    } else 0.dp
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(20.dp)
    ) {
        // Barra do gráfico
        Column(
            modifier = Modifier.height(alturaMaxima)
        ) {
            // Espaço superior para alinhar as barras na parte inferior
            val espacoSuperior = alturaMaxima - alturaInvestido - alturaGanho - alturaPerda
            if (espacoSuperior > 0.dp) {
                Spacer(modifier = Modifier.height(espacoSuperior))
            }
            
            // Ganho de capital (parte superior da barra)
            if (alturaGanho > 0.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(alturaGanho)
                        .background(
                            Color(0xFF81C784),
                            RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                        )
                )
            }
            
            // Valor investido (parte inferior da barra)
            if (alturaInvestido > 0.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(alturaInvestido)
                        .background(
                            Color(0xFF4CAF50),
                            if (alturaGanho > 0.dp) RoundedCornerShape(0.dp)
                            else RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                        )
                )
            }
            
            // Perda (barra vermelha para baixo)
            if (alturaPerda > 0.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(alturaPerda)
                        .background(
                            Color(0xFFE57373),
                            RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp)
                        )
                )
            }
        }
    }
}

/**
 * Componente de gráfico de linha genérico (mantido para compatibilidade)
 */
@Composable
fun LineChartComponent() {
    // Implementação futura se necessário
}