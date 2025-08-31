package br.com.nafer.gerenciadorcripto.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AddToHomeScreen
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Classe que representa uma rota de navegação
 */
sealed class Route(val path: String) {
    /**
     * Rota para a tela inicial
     */
    object Home : Route("home")

    /**
     * Rota para a tela de operações
     */
    object Operacoes : Route("operacoes")

    /**
     * Rota para a tela de relatórios
     */
    object Relatorios : Route("relatorios")

    /**
     * Rota para a tela de importação de CSV
     */
    object ImportacaoCSV : Route("importacao")

    /**
     * Rota para a tela de detalhes de uma operação
     * @param operacaoId ID da operação
     */
    data class DetalhesOperacao(val operacaoId: String) : Route("operacoes/$operacaoId")

    /**
     * Rota para a tela de detalhes de um ativo
     * @param ticker Ticker do ativo
     */
    data class DetalhesAtivo(val ticker: String) : Route("ativos/$ticker")

    data class NovaOperacao(val ticker: String): Route("operacao")

}

/**
 * Obtém o título da rota
 */
fun Route.getTitle(): String {
    return when (this) {
        is Route.Home -> "Carteira"
        is Route.Operacoes -> "Operações"
        is Route.Relatorios -> "Relatórios"
        is Route.ImportacaoCSV -> "Importar"
        is Route.DetalhesOperacao -> "Detalhes da Operação"
        is Route.DetalhesAtivo -> "Detalhes do Ativo"
        is Route.NovaOperacao -> "Nova Operação"
    }
}

/**
 * Obtém o ícone da rota
 */
fun Route.getIcon(): ImageVector? = when (this) {
    is Route.Home -> Icons.AutoMirrored.Default.AddToHomeScreen
    is Route.Operacoes -> Icons.AutoMirrored.Default.List
    is Route.Relatorios -> Icons.AutoMirrored.Default.Assignment
    is Route.ImportacaoCSV -> Icons.AutoMirrored.Default.AddToHomeScreen
    else -> null
}