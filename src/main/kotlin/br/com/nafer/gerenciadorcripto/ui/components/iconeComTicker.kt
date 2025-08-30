package br.com.nafer.gerenciadorcripto.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import java.net.URL

@Composable
fun IconeComTicker(url: String?, ticker: String?, cache: MutableMap<String, ImageBitmap?>, modifier: Modifier) {
    var image by remember(url) { mutableStateOf(cache[url]) }

    LaunchedEffect(url) {
        if (url != null && cache[url] == null) {
            runCatching {
                val bytes = URL(url).readBytes()
                loadImageBitmap(bytes.inputStream())
            }.onSuccess {
                cache[url] = it
                image = it
            }
        }
    }

    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(Color.Transparent)
        ) {
            image?.let {
                Image(
                    painter = BitmapPainter(it),
                    contentDescription = ticker,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
    }
}