package br.com.nafer.gerenciadorcripto.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import br.com.nafer.gerenciadorcripto.ui.components.common.WindowInfo
import br.com.nafer.gerenciadorcripto.ui.screens.*
import org.springframework.context.ConfigurableApplicationContext

/**
 * Componente de navegação que renderiza a tela atual com base na rota
 * @param navigationManager Gerenciador de navegação
 * @param applicationContext Contexto da aplicação Spring
 * @param windowInfo Informações sobre o tamanho da janela para layout responsivo
 */
@Composable
fun Navigation(
    navigationManager: NavigationManager, 
    applicationContext: ConfigurableApplicationContext,
    windowInfo: WindowInfo
) {
    val currentRoute by navigationManager.currentRoute.collectAsState()
    
    when (currentRoute) {
        is Route.Home -> TelaDeCarteiraRefatorada(applicationContext, navigationManager, windowInfo)
        is Route.Operacoes -> telaDeOperacoes(applicationContext, windowInfo)
        is Route.Relatorios -> homeScreen(windowInfo)
        is Route.ImportacaoCSV -> telaDeImportacaoCsv(applicationContext, windowInfo)
        is Route.DetalhesOperacao -> {
            val operacaoId = (currentRoute as Route.DetalhesOperacao).operacaoId
            // Implementar tela de detalhes de operação
            telaDeOperacoes(applicationContext, windowInfo) // Temporário até implementar tela de detalhes
        }
        is Route.DetalhesAtivo -> {
            val ticker = (currentRoute as Route.DetalhesAtivo).ticker
            // Implementar tela de detalhes de ativo
            TelaDeCarteiraRefatorada(applicationContext, navigationManager, windowInfo) // Temporário até implementar tela de detalhes
        }
        is Route.NovaOperacao -> {
            val ticker = (currentRoute as Route.NovaOperacao).ticker
            // Implementar tela de nova operação
            telaDeOperacoes(applicationContext, windowInfo) // Temporário até implementar tela de nova operação
        }
    }
}