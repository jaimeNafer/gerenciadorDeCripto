package br.com.nafer.gerenciadorcripto.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Tamanhos de tela para layout responsivo
 */
enum class ScreenSize {
    COMPACT,  // < 600dp
    MEDIUM,   // 600dp - 840dp
    EXPANDED   // > 840dp
}

/**
 * Hook para obter o tamanho atual da tela
 * @param windowWidth Largura da janela em Dp
 * @return Tamanho da tela (COMPACT, MEDIUM ou EXPANDED)
 */
@Composable
fun rememberScreenSize(windowWidth: Dp): ScreenSize {
    return when {
        windowWidth < 600.dp -> ScreenSize.COMPACT
        windowWidth < 840.dp -> ScreenSize.MEDIUM
        else -> ScreenSize.EXPANDED
    }
}

/**
 * Layout responsivo que adapta o conteúdo com base no tamanho da tela
 * @param compactContent Conteúdo para telas pequenas
 * @param mediumContent Conteúdo para telas médias (opcional, usa compactContent se não fornecido)
 * @param expandedContent Conteúdo para telas grandes (opcional, usa mediumContent se não fornecido)
 */
@Composable
fun ResponsiveLayout(
    modifier: Modifier = Modifier,
    windowWidth: Dp,
    compactContent: @Composable () -> Unit,
    mediumContent: @Composable (() -> Unit)? = null,
    expandedContent: @Composable (() -> Unit)? = null
) {
    val screenSize = rememberScreenSize(windowWidth)
    
    Box(modifier = modifier) {
        when (screenSize) {
            ScreenSize.COMPACT -> compactContent()
            ScreenSize.MEDIUM -> (mediumContent ?: compactContent)()
            ScreenSize.EXPANDED -> (expandedContent ?: mediumContent ?: compactContent)()
        }
    }
}

/**
 * Layout de grade responsiva que ajusta o número de colunas com base no tamanho da tela
 * @param items Lista de itens a serem exibidos
 * @param compactColumns Número de colunas para telas pequenas
 * @param mediumColumns Número de colunas para telas médias
 * @param expandedColumns Número de colunas para telas grandes
 * @param itemContent Conteúdo de cada item
 */
@Composable
fun <T> ResponsiveGrid(
    items: List<T>,
    modifier: Modifier = Modifier,
    windowWidth: Dp,
    compactColumns: Int = 1,
    mediumColumns: Int = 2,
    expandedColumns: Int = 3,
    horizontalSpacing: Dp = 16.dp,
    verticalSpacing: Dp = 16.dp,
    itemContent: @Composable (item: T) -> Unit
) {
    val screenSize = rememberScreenSize(windowWidth)
    
    val columns = when (screenSize) {
        ScreenSize.COMPACT -> compactColumns
        ScreenSize.MEDIUM -> mediumColumns
        ScreenSize.EXPANDED -> expandedColumns
    }
    
    Column(modifier = modifier) {
        var rowItems = mutableListOf<T>()
        
        items.forEachIndexed { index, item ->
            rowItems.add(item)
            
            if (rowItems.size == columns || index == items.size - 1) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    rowItems.forEach { rowItem ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(
                                    end = if (rowItem != rowItems.last()) horizontalSpacing else 0.dp,
                                    bottom = verticalSpacing
                                )
                        ) {
                            itemContent(rowItem)
                        }
                    }
                    
                    // Adiciona espaços vazios para completar a linha
                    if (rowItems.size < columns) {
                        repeat(columns - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
                
                rowItems = mutableListOf()
            }
        }
    }
}

/**
 * Layout adaptativo que alterna entre exibição em linha ou coluna com base no tamanho da tela
 * @param windowWidth Largura da janela em Dp
 * @param breakpoint Ponto de quebra para alternar entre linha e coluna
 * @param horizontalArrangement Arranjo horizontal para o modo de linha
 * @param verticalArrangement Arranjo vertical para o modo de coluna
 * @param content Conteúdo do layout
 */
@Composable
fun AdaptiveLayout(
    modifier: Modifier = Modifier,
    windowWidth: Dp,
    breakpoint: Dp = 600.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    if (windowWidth >= breakpoint) {
        Row(
            modifier = modifier,
            horizontalArrangement = horizontalArrangement
        ) {
            content()
        }
    } else {
        Column(
            modifier = modifier,
            verticalArrangement = verticalArrangement
        ) {
            content()
        }
    }
}