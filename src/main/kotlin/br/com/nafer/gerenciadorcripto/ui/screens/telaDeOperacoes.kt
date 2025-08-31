package br.com.nafer.gerenciadorcripto.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.nafer.gerenciadorcripto.ui.base.UiState
import br.com.nafer.gerenciadorcripto.ui.components.common.EmptyState
import br.com.nafer.gerenciadorcripto.ui.components.common.ErrorState
import br.com.nafer.gerenciadorcripto.ui.components.common.LoadingState
import br.com.nafer.gerenciadorcripto.ui.components.listagemDeOperacoes
import br.com.nafer.gerenciadorcripto.ui.components.listagemDeOperacoesComCabecalhoFixo
import br.com.nafer.gerenciadorcripto.ui.viewmodel.OperacoesEvent
import br.com.nafer.gerenciadorcripto.ui.viewmodel.OperacoesViewModel
import br.com.nafer.gerenciadorcripto.services.IconeCacheService
import org.springframework.context.ConfigurableApplicationContext
import br.com.nafer.gerenciadorcripto.ui.components.common.WindowInfo

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun telaDeOperacoes(applicationContext: ConfigurableApplicationContext, windowInfo: WindowInfo) {
    val viewModel = applicationContext.getBean(OperacoesViewModel::class.java)
    val cacheService = applicationContext.getBean(IconeCacheService::class.java)
    val uiState by viewModel.uiState.collectAsState()
    val filteredState by viewModel.filteredState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Campo de filtro
        OutlinedTextField(
            value = filteredState.filtro,
            onValueChange = { viewModel.processEvent(OperacoesEvent.FiltrarOperacoes(it)) },
            label = { Text("Filtrar operações") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        
        // Conteúdo baseado no estado
        when (uiState) {
            is UiState.Loading -> LoadingState()
            is UiState.Error -> ErrorState(
                message = (uiState as UiState.Error).message,
                onRetry = { viewModel.processEvent(OperacoesEvent.CarregarOperacoes) }
            )
            is UiState.Empty -> EmptyState(
                message = "Nenhuma operação encontrada",
                icon = Icons.Default.Search,
                actionText = "Adicionar primeiro ativo"
            )
            is UiState.Success -> {
                if (filteredState.operacoes.isEmpty()) {
                    EmptyState(
                        message = "Nenhuma operação encontrada com o filtro aplicado",
                        icon = Icons.Default.Search,
                        actionText = "Adicionar primeiro ativo"
                    )
                } else {
                    listagemDeOperacoesComCabecalhoFixo(filteredState.operacoes, cacheService)
                }
            }
        }
    }
}