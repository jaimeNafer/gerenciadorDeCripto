package br.com.nafer.gerenciadorcripto.ui.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun AppTopBar(title: String) {
    TopAppBar(
        title = { Text(title) }
    )
}
