package br.com.nafer.gerenciadorcripto.ui.components.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Componente de cartão com título e conteúdo
 * @param title Título do cartão
 * @param modifier Modificador do cartão
 * @param elevation Elevação do cartão
 * @param backgroundColor Cor de fundo do cartão
 * @param shape Forma do cartão
 * @param onClick Callback para clique no cartão (opcional)
 * @param content Conteúdo do cartão
 */
@Composable
fun TitleCard(
    title: String,
    modifier: Modifier = Modifier,
    elevation: Dp = 4.dp,
    backgroundColor: Color = MaterialTheme.colors.surface,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }
    
    Card(
        modifier = cardModifier,
        elevation = elevation,
        backgroundColor = backgroundColor,
        shape = shape
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

/**
 * Componente de cartão de estatísticas
 * @param title Título da estatística
 * @param value Valor da estatística
 * @param modifier Modificador do cartão
 * @param valueColor Cor do valor (opcional)
 * @param icon Ícone da estatística (opcional)
 */
@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Color.Unspecified,
    icon: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = modifier,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon?.invoke()
            
            Text(
                text = title,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(top = if (icon != null) 8.dp else 0.dp)
            )
            
            Text(
                text = value,
                style = MaterialTheme.typography.h5,
                color = valueColor,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/**
 * Componente de cartão de informações com rótulo e valor
 * @param items Lista de pares de rótulo e valor
 * @param modifier Modificador do cartão
 */
@Composable
fun InfoCard(
    items: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
    title: String,
    backgroundColor: Color
) {
    Card(
        modifier = modifier,
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            items.forEachIndexed { index, (label, value) ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.body1
                    )
                }
                
                if (index < items.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}