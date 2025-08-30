package br.com.nafer.gerenciadorcripto.ui.viewmodel

import br.com.nafer.gerenciadorcripto.domain.model.dtos.OperacoesDTO
import br.com.nafer.gerenciadorcripto.dtos.ListagemDeOperacoesDTO
import br.com.nafer.gerenciadorcripto.service.OperacaoService
import br.com.nafer.gerenciadorcripto.ui.base.BaseViewModel
import br.com.nafer.gerenciadorcripto.ui.base.ServiceAdapter
import br.com.nafer.gerenciadorcripto.ui.base.UiEvent
import br.com.nafer.gerenciadorcripto.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.springframework.stereotype.Component

/**
 * Eventos da UI para a tela de operações
 */
sealed class OperacoesEvent : UiEvent {
    object CarregarOperacoes : OperacoesEvent()
    data class FiltrarOperacoes(val filtro: String) : OperacoesEvent()
}

/**
 * Estado da UI para a tela de operações
 */
data class OperacoesUiState(
    val operacoes: List<ListagemDeOperacoesDTO> = emptyList(),
    val filtro: String = ""
)

/**
 * ViewModel para a tela de operações
 */
@Component
class OperacoesViewModel(private val operacaoService: OperacaoService) : BaseViewModel<OperacoesUiState, OperacoesEvent>() {
    
    // Estado filtrado para a UI
    private val _filteredState = MutableStateFlow(OperacoesUiState())
    val filteredState: StateFlow<OperacoesUiState> = _filteredState.asStateFlow()
    
    init {
        // Carrega as operações ao inicializar o ViewModel
        processEvent(OperacoesEvent.CarregarOperacoes)
    }
    
    override fun processEvent(event: OperacoesEvent) {
        when (event) {
            is OperacoesEvent.CarregarOperacoes -> carregarOperacoes()
            is OperacoesEvent.FiltrarOperacoes -> filtrarOperacoes(event.filtro)
        }
    }
    
    private fun carregarOperacoes() {
        launch {
            // Usa o ServiceAdapter para converter a chamada síncrona em Flow<UiState>
            ServiceAdapter.adapt {
                operacaoService.listarOperacoes().map { converterParaDTO(it) }
            }.collect { state ->
                when (state) {
                    is UiState.Loading -> updateState(UiState.Loading)
                    is UiState.Success -> {
                        val uiState = OperacoesUiState(operacoes = state.data)
                        updateState(UiState.Success(uiState))
                        _filteredState.value = uiState
                    }
                    is UiState.Error -> updateState(UiState.Error(state.message))
                    is UiState.Empty -> updateState(UiState.Empty)
                }
            }
        }
    }
    
    private fun filtrarOperacoes(filtro: String) {
        launch {
            // Obtém o estado atual se for Success
            val currentState = uiState.value
            if (currentState is UiState.Success) {
                val operacoesFiltradas = if (filtro.isBlank()) {
                    currentState.data.operacoes
                } else {
                    currentState.data.operacoes.filter { operacao ->
                        operacao.ativoEntrada?.contains(filtro, ignoreCase = true) == true ||
                        operacao.ativoSaida?.contains(filtro, ignoreCase = true) == true ||
                        operacao.tipo?.contains(filtro, ignoreCase = true) == true ||
                        operacao.operacao?.contains(filtro, ignoreCase = true) == true
                    }
                }
                
                _filteredState.value = _filteredState.value.copy(
                    operacoes = operacoesFiltradas,
                    filtro = filtro
                )
            }
        }
    }
    
    private fun converterParaDTO(operacao: OperacoesDTO): ListagemDeOperacoesDTO {
        val dataOperacao = operacao.dataOperacaoEntrada ?: operacao.dataOperacaoSaida
        return ListagemDeOperacoesDTO(
            dataOperacao!!,
            operacao.moedaEntrada?.ticker,
            operacao.moedaEntrada?.icone,
            operacao.moedaEntrada?.nome,
            operacao.quantidadeEntrada,
            operacao.moedaSaida?.ticker,
            operacao.moedaSaida?.icone,
            operacao.moedaSaida?.nome,
            operacao.quantidadeSaida,
            operacao.valorBrl,
            operacao.lucroPrejuizo,
            operacao.tipoOperacao.descricao,
            operacao.finalidade.descricao,
            operacao.tipoOperacao
        )
    }
}