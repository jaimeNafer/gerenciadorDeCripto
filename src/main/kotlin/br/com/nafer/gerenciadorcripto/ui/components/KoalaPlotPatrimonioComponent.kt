package br.com.nafer.gerenciadorcripto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import br.com.nafer.gerenciadorcripto.domain.model.dtos.EvolucaoPatrimonioDTO

@Composable
fun KoalaPlotPatrimonio(
    dados: List<EvolucaoPatrimonioDTO>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Evolução do Patrimônio",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (dados.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum dado disponível",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        } else {
            GraficoEvolucaoPatrimonio(
                dados = dados,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            
            // Resumo dos dados
            if (dados.isNotEmpty()) {
                val valorInvestido = dados.lastOrNull()?.valorInvestido?.toDouble() ?: 0.0
                val valorTotal = dados.lastOrNull()?.valorTotal?.toDouble() ?: 0.0
                val ganhoTotal = dados.lastOrNull()?.ganhoCapital?.toDouble() ?: 0.0
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Total Investido",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "R$ %.2f".format(valorInvestido),
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Valor Atual",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "R$ %.2f".format(valorTotal),
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Ganho/Perda",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "R$ %.2f".format(ganhoTotal),
                            fontSize = 16.sp,
                            color = if (ganhoTotal >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GraficoEvolucaoPatrimonio(
    dados: List<EvolucaoPatrimonioDTO>,
    modifier: Modifier = Modifier
) {
    // Por enquanto, vamos mostrar um gráfico simples com texto
    // até que o KoalaPlot seja configurado corretamente
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📊 Gráfico de Evolução do Patrimônio",
                fontSize = 16.sp,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (dados.isNotEmpty()) {
                Text(
                    text = "${dados.size} pontos de dados disponíveis",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                val valorInicial = dados.firstOrNull()?.valorTotal?.toDouble() ?: 0.0
                val valorFinal = dados.lastOrNull()?.valorTotal?.toDouble() ?: 0.0
                val variacao = valorFinal - valorInicial
                val percentual = if (valorInicial > 0) (variacao / valorInicial) * 100 else 0.0
                
                Text(
                    text = "Variação: R$ %.2f (%.1f%%)".format(variacao, percentual),
                    fontSize = 14.sp,
                    color = if (variacao >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            } else {
                Text(
                    text = "Nenhum dado disponível",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}