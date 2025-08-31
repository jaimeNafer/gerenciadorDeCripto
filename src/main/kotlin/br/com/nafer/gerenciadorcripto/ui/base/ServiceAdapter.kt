package br.com.nafer.gerenciadorcripto.ui.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

/**
 * Adaptador para converter chamadas de serviços Spring em Flows para uso com MVVM
 */
object ServiceAdapter {
    
    /**
     * Converte uma chamada de serviço síncrona em um Flow que emite estados de UI
     * @param serviceCall Função que faz a chamada ao serviço
     * @return Flow que emite estados de UI (Loading, Success, Error)
     */
    fun <T> adapt(serviceCall: () -> T): Flow<UiState<T>> = flow {
        emit(UiState.Loading)
        try {
            val result = withContext(Dispatchers.IO) {
                serviceCall()
            }
            if (isEmptyResult(result)) {
                emit(UiState.Empty)
            } else {
                emit(UiState.Success(result))
            }
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Erro desconhecido"))
        }
    }
    
    /**
     * Verifica se o resultado é considerado vazio
     */
    private fun <T> isEmptyResult(result: T): Boolean {
        return when (result) {
            null -> true
            is Collection<*> -> result.isEmpty()
            is Map<*, *> -> result.isEmpty()
            is Array<*> -> result.isEmpty()
            is String -> result.isEmpty()
            else -> false
        }
    }
    
    /**
     * Executa uma operação de serviço e retorna um Flow com o resultado
     * @param operation Operação a ser executada
     * @return Flow com o resultado da operação
     */
    suspend fun <T> executeOperation(operation: suspend () -> T): UiState<T> {
        return try {
            val result = withContext(Dispatchers.IO) {
                operation()
            }
            if (isEmptyResult(result)) {
                UiState.Empty
            } else {
                UiState.Success(result)
            }
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Erro desconhecido")
        }
    }
}