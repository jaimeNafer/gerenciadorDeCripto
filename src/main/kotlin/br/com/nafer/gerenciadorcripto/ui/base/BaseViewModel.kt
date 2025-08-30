package br.com.nafer.gerenciadorcripto.ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel base que implementa o padrão MVVM
 * @param S Tipo do estado da UI
 * @param E Tipo dos eventos da UI
 */
abstract class BaseViewModel<S, E> {
    
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    // Estado da UI
    private val _uiState = MutableStateFlow<UiState<S>>(UiState.Loading)
    val uiState: StateFlow<UiState<S>> = _uiState.asStateFlow()
    
    /**
     * Atualiza o estado da UI
     */
    protected fun updateState(state: UiState<S>) {
        _uiState.value = state
    }
    
    /**
     * Lança uma coroutine no escopo do ViewModel
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(block = block)
    }
    
    /**
     * Processa um evento da UI
     */
    abstract fun processEvent(event: E)
    
    /**
     * Limpa recursos quando o ViewModel não é mais necessário
     */
    fun clear() {
        viewModelScope.cancel()
    }
}