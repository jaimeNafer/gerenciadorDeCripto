package br.com.nafer.gerenciadorcripto.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Gerenciador de navegação que controla o backstack e a navegação entre telas
 */
class NavigationManager {
    private val _currentRoute = MutableStateFlow<Route>(Route.Home)
    val currentRoute: StateFlow<Route> = _currentRoute.asStateFlow()

    private val _backStack = MutableStateFlow<List<Route>>(listOf(Route.Home))
    val backStack: StateFlow<List<Route>> = _backStack.asStateFlow()

    /**
     * Navega para uma rota
     * @param route Rota para a qual navegar
     * @param addToBackStack Se a rota atual deve ser adicionada ao backstack
     */
    fun navigateTo(route: Route, addToBackStack: Boolean = true) {
        if (addToBackStack) {
            _backStack.value = _backStack.value + _currentRoute.value
        }
        _currentRoute.value = route
    }

    /**
     * Navega para trás no backstack
     * @return true se foi possível navegar para trás, false caso contrário
     */
    fun navigateBack(): Boolean {
        if (_backStack.value.isEmpty()) {
            return false
        }

        val newBackStack = _backStack.value.toMutableList()
        val previousRoute = newBackStack.removeLastOrNull() ?: return false
        
        _currentRoute.value = previousRoute
        _backStack.value = newBackStack
        
        return true
    }

    /**
     * Limpa o backstack
     */
    fun clearBackStack() {
        _backStack.value = emptyList()
    }
}