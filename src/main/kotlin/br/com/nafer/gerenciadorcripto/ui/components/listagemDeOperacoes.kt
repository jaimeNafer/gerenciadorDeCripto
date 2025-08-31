package br.com.nafer.gerenciadorcripto.ui.components

import Formatador
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.nafer.gerenciadorcripto.domain.model.enums.TipoOperacaoEnum
import br.com.nafer.gerenciadorcripto.dtos.ListagemDeOperacoesDTO
import br.com.nafer.gerenciadorcripto.services.IconeCacheService
import java.math.BigDecimal

@Composable
fun listagemDeOperacoes(operacoes: List<ListagemDeOperacoesDTO>, cacheService: IconeCacheService) {
    val groupedByMonth = operacoes.groupBy { mov ->
        "${mov.dataOperacao.month.value}/${mov.dataOperacao.year}"
    }

    // Map para guardar o estado de expandido de cada mês
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }

    LazyColumn {
        groupedByMonth.forEach { (month, movsDoMes) ->
            item {
                // Inicializa como expandido por padrão
                val expanded = expandedStates.getOrPut(month) { true }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                ) {
                    // Cabeçalho clicável
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedStates[month] = !expanded }
                            .background(Color(0xFFEEEEEE))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "📅 ${formatarMesAno(month)}",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            if (expanded) "˄" else "˅",
                            style = MaterialTheme.typography.h5
                        )
                    }

                    // Resumo do mês
                    if (expanded) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Lucro/Prejuízo: R$ ${movsDoMes.sumOf { it.lucroPrejuizo ?: BigDecimal.ZERO }}")
                            Text("Total movimentado: R$ ${movsDoMes.sumOf { it.valorBrl ?: BigDecimal.ZERO }}")
                            Text("IN1888 necessária: ${if (movsDoMes.sumOf { it.valorBrl ?: BigDecimal.ZERO }.compareTo(BigDecimal("35000")) >= 0) "SIM" else "NÃO"}")
                            Text("Total operações: ${movsDoMes.size}")
                        }
                    }

                    // Lista de operacoes com AnimatedVisibility
                    AnimatedVisibility(visible = expanded) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.LightGray)
                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                            ) {
                                Text("Data", Modifier.weight(1f))
                                Text("Operação", Modifier.weight(1f))
                                Text("Moeda Entrada", Modifier.weight(1f))
                                Text("Qtd Entrada", Modifier.weight(1f))
                                Text("Moeda Saída", Modifier.weight(1f))
                                Text("Qtd Saída", Modifier.weight(1f))
                                Text("Valor (BRL)", Modifier.weight(1f))
                                Text("Lucro/Prejuízo", Modifier.weight(1f))
                            }
                            Divider(color = Color.DarkGray)
                            movsDoMes.forEach { mov ->
                                SelectionContainer {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(corDeFundoParaOperacao(mov.tipoOperacao))
                                            .padding(vertical = 4.dp, horizontal = 8.dp)
                                    ) {
                                        Text(Formatador.formatarData(mov.dataOperacao), Modifier.weight(1f))
                                        TipoComOperacao(mov.tipo, mov.operacao, Modifier.weight(1f))
                                        IconeComTickerENome(mov.iconeAtivoEntrada, mov.ativoEntrada, mov.nomeAtivoEntrada, cacheService, Modifier.weight(1f))
                                        Text(Formatador.formatarQuantidade(mov.quantidadeEntrada), Modifier.weight(1f))
                                        IconeComTickerENome(mov.iconeAtivoSaida, mov.ativoSaida, mov.nomeAtivoSaida, cacheService, Modifier.weight(1f))
                                        Text(Formatador.formatarQuantidade(mov.quantidadeSaida), Modifier.weight(1f))
                                        Text(Formatador.formatarMoeda(mov.valorBrl), Modifier.weight(1f))
                                        Text(Formatador.formatarMoeda(mov.lucroPrejuizo), Modifier.weight(1f))
                                    }
                                }
                                Divider()
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

fun formatarMesAno(data: String): String {
    val dataMesAno = data.split('/')

    val nomeMes = when (dataMesAno[0]) {
        "1" -> "Janeiro"
        "2" -> "Fevereiro"
        "3" -> "Março"
        "4" -> "Abril"
        "5" -> "Maio"
        "6" -> "Junho"
        "7" -> "Julho"
        "8" -> "Agosto"
        "9" -> "Setembro"
        "10" -> "Outubro"
        "11" -> "Novembro"
        "12" -> "Dezembro"
        else -> dataMesAno[0]
    }
    return "$nomeMes/${dataMesAno[1]}"
}

fun corDeFundoParaOperacao(tipo: TipoOperacaoEnum): Color {
    return when (tipo) {
        TipoOperacaoEnum.COMPRA -> Color(0xFFE8F5E9)
        TipoOperacaoEnum.VENDA -> Color(0xFFF3E5F5)
        TipoOperacaoEnum.PERMUTA -> Color(0xFFFFF9C4)
        else -> Color(0xFFF5F5F5)
    }
}

private fun corDeFundoParaOperacao(operacao: String): Color {
    return when (operacao.lowercase()) {
        "compra" -> Color(0xFF4CAF50).copy(alpha = 0.1f) // Verde claro
        "venda" -> Color(0xFFF44336).copy(alpha = 0.1f) // Vermelho claro
        "saque" -> Color(0xFFFF9800).copy(alpha = 0.1f) // Laranja claro
        "depósito" -> Color(0xFF2196F3).copy(alpha = 0.1f) // Azul claro
        else -> Color.Transparent
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun listagemDeOperacoesComCabecalhoFixo(
    operacoes: List<ListagemDeOperacoesDTO>,
    cacheService: IconeCacheService
) {
    val groupedByMonth = operacoes.groupBy { mov ->
        "${mov.dataOperacao.month.value}/${mov.dataOperacao.year}"
    }
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }
    
    // Calcular totais gerais
    val totalGeralLucroPrejuizo = operacoes.sumOf { it.lucroPrejuizo ?: BigDecimal.ZERO }
    val totalGeralMovimentado = operacoes.sumOf { it.valorBrl ?: BigDecimal.ZERO }
    val totalGeralOperacoes = operacoes.size
    val necessitaIN1888Geral = totalGeralMovimentado.compareTo(BigDecimal("35000")) >= 0
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Cabeçalho fixo consolidado geral
        stickyHeader {
            Column(
                 modifier = Modifier
                     .fillMaxWidth()
                     .background(Color(0xFFE3F2FD))
                     .padding(12.dp)
             ) {
                Text(
                    text = "📊 Consolidado Geral",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.primary
                )

                Column(modifier = Modifier.padding(top = 6.dp)) {
                    Text(
                        text = "Lucro/Prejuízo: ${Formatador.formatarMoeda(totalGeralLucroPrejuizo)}",
                        style = MaterialTheme.typography.body2,
                        color = if (totalGeralLucroPrejuizo.compareTo(BigDecimal.ZERO) >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                    Text(
                        text = "Total movimentado: ${Formatador.formatarMoeda(totalGeralMovimentado)}",
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = "IN1888 necessária: ${if (necessitaIN1888Geral) "SIM" else "NÃO"}",
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = "Total operações: $totalGeralOperacoes",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
        
        // Conteúdo das operações agrupadas por mês
        groupedByMonth.forEach { (mesAno, operacoesDoMes) ->
            // Lista de operações do mês
                // Cabeçalho fixo da tabela
                stickyHeader {
                    Column {
                        // Cabeçalho do mês (não fixo)
                        Row {
                            val isExpanded = expandedStates[mesAno] ?: true
                            val totalLucroPrejuizo = operacoesDoMes.sumOf { it.lucroPrejuizo ?: BigDecimal.ZERO }
                            val totalMovimentado = operacoesDoMes.sumOf { it.valorBrl ?: BigDecimal.ZERO }
                            val totalOperacoes = operacoesDoMes.size
                            val necessitaIN1888 = totalMovimentado.compareTo(BigDecimal("35000")) >= 0

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFEEEEEE))
                                    .clickable { expandedStates[mesAno] = !isExpanded }
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "📅 ${formatarMesAno(mesAno)}",
                                        style = MaterialTheme.typography.h6,
                                        color = MaterialTheme.colors.primary
                                    )
                                    Text(
                                        text = if (isExpanded) "˄" else "˅",
                                        style = MaterialTheme.typography.h5,
                                        color = MaterialTheme.colors.primary
                                    )
                                }

                                // Informações consolidadas do mês
                                if (isExpanded) {
                                    Column(modifier = Modifier.padding(top = 6.dp)) {
                                        Text(
                                            text = "Lucro/Prejuízo: ${Formatador.formatarMoeda(totalLucroPrejuizo)}",
                                            style = MaterialTheme.typography.body2,
                                            color = if (totalLucroPrejuizo.compareTo(BigDecimal.ZERO) >= 0) Color(
                                                0xFF4CAF50
                                            ) else Color(0xFFF44336)
                                        )
                                        Text(
                                            text = "Total movimentado: ${Formatador.formatarMoeda(totalMovimentado)}",
                                            style = MaterialTheme.typography.body2
                                        )
                                        Text(
                                            text = "IN1888 necessária: ${if (necessitaIN1888) "SIM" else "NÃO"}",
                                            style = MaterialTheme.typography.body2
                                        )
                                        Text(
                                            text = "Total operações: $totalOperacoes",
                                            style = MaterialTheme.typography.body2
                                        )
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFDDDDDD))
                                .padding(vertical = 6.dp, horizontal = 6.dp)
                        ) {
                            Text("Data", Modifier.weight(0.8f), style = MaterialTheme.typography.subtitle2)
                            Text("Operação", Modifier.weight(0.8f), style = MaterialTheme.typography.subtitle2)
                            Text("Moeda Entrada", Modifier.weight(1f), style = MaterialTheme.typography.subtitle2)
                            Text("Qtd Entrada", Modifier.weight(0.8f), style = MaterialTheme.typography.subtitle2)
                            Text("Moeda Saída", Modifier.weight(1f), style = MaterialTheme.typography.subtitle2)
                            Text("Qtd Saída", Modifier.weight(0.8f), style = MaterialTheme.typography.subtitle2)
                            Text("Valor (BRL)", Modifier.weight(0.9f), style = MaterialTheme.typography.subtitle2)
                            Text("Lucro/Prejuízo", Modifier.weight(0.9f), style = MaterialTheme.typography.subtitle2)
                        }
                    }
                }
                items(operacoesDoMes) { operacao ->
                    SelectionContainer {
                        Row(
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .background(corDeFundoParaOperacao(operacao.tipoOperacao))
                                 .padding(vertical = 3.dp, horizontal = 6.dp)
                         ) {
                             Text(
                                 text = Formatador.formatarData(operacao.dataOperacao),
                                 style = MaterialTheme.typography.body2,
                                 modifier = Modifier.weight(0.8f)
                             )
                             
                             TipoComOperacao(
                                 tipo = operacao.tipo,
                                 operacao = operacao.operacao,
                                 modifier = Modifier.weight(0.8f)
                             )
                             
                             IconeComTickerENome(
                                 url = operacao.iconeAtivoEntrada,
                                 ticker = operacao.ativoEntrada,
                                 nome = operacao.nomeAtivoEntrada,
                                 cacheService = cacheService,
                                 modifier = Modifier.weight(1f)
                             )
                             
                             Text(
                                 text = Formatador.formatarQuantidade(operacao.quantidadeEntrada),
                                 style = MaterialTheme.typography.body2,
                                 modifier = Modifier.weight(0.8f)
                             )
                             
                             IconeComTickerENome(
                                 url = operacao.iconeAtivoSaida,
                                 ticker = operacao.ativoSaida,
                                 nome = operacao.nomeAtivoSaida,
                                 cacheService = cacheService,
                                 modifier = Modifier.weight(1f)
                             )
                             
                             Text(
                                 text = Formatador.formatarQuantidade(operacao.quantidadeSaida),
                                 style = MaterialTheme.typography.body2,
                                 modifier = Modifier.weight(0.8f)
                             )
                             
                             Text(
                                 text = Formatador.formatarMoeda(operacao.valorBrl),
                                 style = MaterialTheme.typography.body2,
                                 modifier = Modifier.weight(0.9f)
                             )
                             
                             Text(
                                 text = Formatador.formatarMoeda(operacao.lucroPrejuizo),
                                 style = MaterialTheme.typography.body2,
                                 color = if ((operacao.lucroPrejuizo?.compareTo(BigDecimal.ZERO) ?: -1) >= 0) Color(0xFF4CAF50) else Color(0xFFF44336),
                                 modifier = Modifier.weight(0.9f)
                             )
                         }
                    }
                    Divider()
                }

        }
    }
}