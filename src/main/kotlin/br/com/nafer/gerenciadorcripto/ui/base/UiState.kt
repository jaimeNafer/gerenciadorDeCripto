package br.com.nafer.gerenciadorcripto.ui.base

/**
 * Classe selada que representa os diferentes estados de uma UI
 */
sealed class UiState<out T> {
    /**
     * Estado inicial ou de carregamento
     */
    object Loading : UiState<Nothing>()

    /**
     * Estado de sucesso com dados
     */
    data class Success<T>(val data: T) : UiState<T>()

    /**
     * Estado de erro
     */
    data class Error(val message: String, val exception: Throwable? = null) : UiState<Nothing>()

    /**
     * Estado vazio (sem dados)
     */
    object Empty : UiState<Nothing>()
}