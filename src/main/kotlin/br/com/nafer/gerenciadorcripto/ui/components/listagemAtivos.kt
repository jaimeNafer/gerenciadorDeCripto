package br.com.nafer.gerenciadorcripto.ui.components

import Formatador
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.nafer.gerenciadorcripto.ui.screens.AtivoCarteiraDTO
import br.com.nafer.gerenciadorcripto.services.IconeCacheService
import org.springframework.context.ConfigurableApplicationContext

@Composable
fun listagemAtivos(
    ativos: List<AtivoCarteiraDTO>,
    onNovaOperacao: (AtivoCarteiraDTO) -> Unit,
    onEditarOperacao: (AtivoCarteiraDTO) -> Unit,
    cacheService: IconeCacheService
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        // Cabeçalho da tabela
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFEFEF))
                    .padding(vertical = 6.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ativo", modifier = Modifier.weight(2f))
                Text("Qtd", modifier = Modifier.weight(1f))
                Text("Preço Médio", modifier = Modifier.weight(1f))
                Text("Preço Atual", modifier = Modifier.weight(1f))
                Text("Investido", modifier = Modifier.weight(1f))
                Text("Lucro/Prejuízo", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
        }

        // Lista de ativos com scroll
        items(ativos) { ativo ->
            var expanded by remember { mutableStateOf(false) }

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(2f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconeComTicker(
                            url = ativo.icone,
                            ticker = ativo.ticker,
                            cacheService = cacheService,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(ativo.ticker, style = MaterialTheme.typography.body2)
                            Text(ativo.nome, fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                    Text(
                        text = Formatador.formatarQuantidade(ativo.quantidade),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = Formatador.formatarMoeda(ativo.precoMedio),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = Formatador.formatarMoeda(ativo.valorAtual),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = Formatador.formatarMoeda(ativo.valorInvestido),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = Formatador.formatarMoeda(ativo.lucroPrejuizo),
                        modifier = Modifier.weight(1f),
                        color = colorFromValue(ativo.lucroPrejuizo),
                        style = MaterialTheme.typography.body2
                    )

                    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Ações")
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(onClick = {
                                expanded = false
                                onNovaOperacao(ativo)
                            }) {
                                Text("Nova operação")
                            }
                            DropdownMenuItem(onClick = {
                                expanded = false
                                onEditarOperacao(ativo)
                            }) {
                                Text("Editar")
                            }
                        }
                    }
                }
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }
    }
}
