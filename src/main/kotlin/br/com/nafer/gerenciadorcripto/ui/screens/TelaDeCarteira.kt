package br.com.nafer.gerenciadorcripto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.ExperimentalFoundationApi
import Formatador
import br.com.nafer.gerenciadorcripto.ui.components.colorFromValue
import br.com.nafer.gerenciadorcripto.ui.components.IconeComTicker
import br.com.nafer.gerenciadorcripto.navigation.NavigationManager
import br.com.nafer.gerenciadorcripto.navigation.Route
import br.com.nafer.gerenciadorcripto.ui.base.UiState
import br.com.nafer.gerenciadorcripto.ui.components.ResumoCarteira
import br.com.nafer.gerenciadorcripto.ui.components.common.*
import br.com.nafer.gerenciadorcripto.ui.components.graficoEvolucaoPatrimonioChart
import br.com.nafer.gerenciadorcripto.ui.components.listagemAtivos
import br.com.nafer.gerenciadorcripto.ui.viewmodel.CarteiraEvent
import br.com.nafer.gerenciadorcripto.ui.viewmodel.CarteiraViewModel
import br.com.nafer.gerenciadorcripto.services.IconeCacheService
import org.springframework.context.ConfigurableApplicationContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TelaDeCarteiraRefatorada(
    applicationContext: ConfigurableApplicationContext,
    navigationManager: NavigationManager,
    windowInfo: WindowInfo
) {
    val viewModel = remember { applicationContext.getBean(CarteiraViewModel::class.java) }
    val cacheService = remember { applicationContext.getBean(IconeCacheService::class.java) }
    val uiState by viewModel.carteiraState.collectAsState()
    val filteredAtivos by viewModel.filteredAtivos.collectAsState()
    
    // Configurar callbacks de navegação
    LaunchedEffect(viewModel) {
        viewModel.onNavigateToNovaOperacao = { ticker ->
            navigationManager.navigateTo(Route.NovaOperacao(ticker))
        }
        viewModel.onNavigateToVerOperacoes = { ticker ->
            navigationManager.navigateTo(Route.DetalhesAtivo(ticker))
        }
    }
    
    // Carregar dados ao iniciar
    LaunchedEffect(Unit) {
        viewModel.processEvent(CarteiraEvent.LoadCarteira)
    }
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Barra de pesquisa
        StandardTextField(
            value = uiState.filtroAtivos,
            onValueChange = { viewModel.processEvent(CarteiraEvent.FilterAtivos(it)) },
            label = "Buscar ativos",
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        )
        
        // Conteúdo principal
        when (filteredAtivos) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingIndicator()
                }
            }
            is UiState.Error -> {
                val errorState = filteredAtivos as UiState.Error
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    InfoCard(
                        title = "Erro ao carregar carteira",
                        items = listOf("Mensagem" to errorState.message),
                        backgroundColor = Color(0xFFFFEBEE)
                    )
                }
            }
            is UiState.Empty -> {
                if (uiState.filtroAtivos.isNotBlank()) {
                    // Sem resultados para o filtro
                    EmptyState(
                        message = "Nenhum ativo encontrado para '${uiState.filtroAtivos}'",
                        icon = Icons.Default.Search,
                        actionText = "Adicionar primeiro ativo"
                    )
                } else {
                    // Carteira vazia
                    EmptyState(
                        message = "Sua carteira está vazia",
                        actionText = "Adicionar primeiro ativo"
                    )
                }
            }
            is UiState.Success -> {
                val ativos = (filteredAtivos as UiState.Success<List<AtivoCarteiraDTO>>).data
                
                // Layout com scroll completo da tela
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Resumo da carteira
                    item {
                        ResumoCarteira(
                            valorAtual = uiState.patrimonio.valorAtual,
                            valorInvestido = uiState.patrimonio.valorInvestido,
                            lucroPrejuizo = uiState.patrimonio.lucroPrejuizo,
                            rentabilidadePercentual = uiState.patrimonio.percentualVariacao
                        )
                    }
                    
                    item {
                        Spacer(Modifier.height(16.dp))
                    }
                    
                    // Gráfico de evolução do patrimônio
                    item {
                        Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth().height(if (windowInfo.isExpanded) 300.dp else 220.dp)) {
                            graficoEvolucaoPatrimonioChart(uiState.historico)
                        }
                    }
                    
                    item {
                        Spacer(Modifier.height(16.dp))
                    }
                    
                    // Título da seção de ativos
                    item {
                        Text("Meus Ativos", style = MaterialTheme.typography.h6, modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))
                    }
                    
                    // Cabeçalho fixo da tabela de ativos
                    stickyHeader {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEFEFEF))
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Ativo", modifier = Modifier.weight(2f))
                            Text("Qtd", modifier = Modifier.weight(1f))
                            Text("Preço Médio", modifier = Modifier.weight(1f))
                            Text("Preço Atual", modifier = Modifier.weight(1f))
                            Text("Investido", modifier = Modifier.weight(1f))
                            Text("Lucro/Prejuízo", modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(32.dp))
                        }
                        Divider(color = Color.LightGray, thickness = 1.dp)
                    }
                    
                    // Lista de ativos
                    items(ativos) { ativo ->
                        var expanded by remember { mutableStateOf(false) }
                        
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
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
                                    Spacer(modifier = Modifier.width(8.dp))
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
                                            viewModel.processEvent(CarteiraEvent.NavigateToNovaOperacao(ativo))
                                        }) {
                                            Text("Nova operação")
                                        }
                                        DropdownMenuItem(onClick = {
                                            expanded = false
                                            viewModel.processEvent(CarteiraEvent.NavigateToVerOperacoes(ativo))
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
        }
    }
}
