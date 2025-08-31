package br.com.nafer.gerenciadorcripto.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Componente de tabela de dados genérica
 * @param headers Lista de cabeçalhos da tabela
 * @param data Lista de dados a serem exibidos
 * @param rowContent Conteúdo de cada linha
 */
@Composable
fun <T> DataTable(
    headers: List<String>,
    data: List<T>,
    modifier: Modifier = Modifier,
    headerBackgroundColor: Color = Color.LightGray,
    rowContent: @Composable (RowScope.(item: T) -> Unit)
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Cabeçalho da tabela
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerBackgroundColor)
                .padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            headers.forEach { header ->
                Text(
                    text = header,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
            }
        }
        
        Divider(color = Color.DarkGray)
        
        // Dados da tabela
        if (data.isEmpty()) {
            EmptyState("Nenhum dado disponível", icon = Icons.Default.Search, actionText = "Adicionar primeiro ativo")
        } else {
            LazyColumn {
                items(data) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                    ) {
                        rowContent(item)
                    }
                    Divider()
                }
            }
        }
    }
}

/**
 * Componente de célula de tabela
 */
@Composable
fun TableCell(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.Unspecified
) {
    Text(
        text = text,
        modifier = modifier.padding(vertical = 4.dp),
        textAlign = textAlign,
        fontWeight = fontWeight,
        color = color
    )
}