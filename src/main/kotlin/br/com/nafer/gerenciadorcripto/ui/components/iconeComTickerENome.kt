package br.com.nafer.gerenciadorcripto.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.nafer.gerenciadorcripto.services.IconeCacheService

@Composable
fun IconeComTickerENome(
    url: String?,
    ticker: String?,
    nome: String?,
    cacheService: IconeCacheService,
    modifier: Modifier = Modifier
) {
    if (ticker != null) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconeComTicker(
                url = url,
                ticker = ticker,
                cacheService = cacheService,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(ticker, style = MaterialTheme.typography.body2)
                if (nome != null && nome != ticker) {
                    Text(nome, fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
    } else {
        Text("-", modifier = modifier)
    }
}