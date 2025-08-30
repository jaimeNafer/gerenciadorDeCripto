package br.com.nafer.gerenciadorcripto.ui.components.common

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.takeOrElse
import androidx.compose.ui.window.WindowState

/**
 * Classe que contém informações sobre o tamanho da janela
 * @param width Largura da janela em Dp
 * @param height Altura da janela em Dp
 * @param screenSize Tamanho da tela (COMPACT, MEDIUM ou EXPANDED)
 */
data class WindowInfo(
    val width: Dp,
    val height: Dp,
    val screenSize: ScreenSize
)

/**
 * Hook para obter informações sobre o tamanho da janela
 * @param windowState Estado da janela
 * @return WindowInfo com informações sobre o tamanho da janela
 */
@Composable
fun rememberWindowInfo(windowState: WindowState): WindowInfo {
    val density = LocalDensity.current
    
    // Converte pixels para Dp
    val widthDp = with(density) { windowState.size.width.value.toDp() }
    val heightDp = with(density) { windowState.size.height.value.toDp() }
    
    // Determina o tamanho da tela
    val screenSize = rememberScreenSize(widthDp)
    
    return remember(widthDp, heightDp, screenSize) {
        WindowInfo(widthDp, heightDp, screenSize)
    }
}

/**
 * Extensão para verificar se o tamanho da tela é compacto
 */
val WindowInfo.isCompact: Boolean
    get() = screenSize == ScreenSize.COMPACT

/**
 * Extensão para verificar se o tamanho da tela é médio
 */
val WindowInfo.isMedium: Boolean
    get() = screenSize == ScreenSize.MEDIUM

/**
 * Extensão para verificar se o tamanho da tela é expandido
 */
val WindowInfo.isExpanded: Boolean
    get() = screenSize == ScreenSize.EXPANDED

/**
 * Extensão para obter o número de colunas recomendado com base no tamanho da tela
 * @param compactColumns Número de colunas para telas compactas
 * @param mediumColumns Número de colunas para telas médias
 * @param expandedColumns Número de colunas para telas expandidas
 * @return Número de colunas recomendado
 */
fun WindowInfo.getColumnCount(
    compactColumns: Int = 1,
    mediumColumns: Int = 2,
    expandedColumns: Int = 3
): Int {
    return when (screenSize) {
        ScreenSize.COMPACT -> compactColumns
        ScreenSize.MEDIUM -> mediumColumns
        ScreenSize.EXPANDED -> expandedColumns
    }
}

/**
 * Extensão para obter o espaçamento recomendado com base no tamanho da tela
 * @param compactSpacing Espaçamento para telas compactas
 * @param mediumSpacing Espaçamento para telas médias
 * @param expandedSpacing Espaçamento para telas expandidas
 * @return Espaçamento recomendado em Dp
 */
fun WindowInfo.getSpacing(
    compactSpacing: Dp = 8.dp,
    mediumSpacing: Dp = 16.dp,
    expandedSpacing: Dp = 24.dp
): Dp {
    return when (screenSize) {
        ScreenSize.COMPACT -> compactSpacing
        ScreenSize.MEDIUM -> mediumSpacing
        ScreenSize.EXPANDED -> expandedSpacing
    }
}