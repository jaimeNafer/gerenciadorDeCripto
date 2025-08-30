package br.com.nafer.gerenciadorcripto.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AddToHomeScreen
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(val title: String, val icone: ImageVector) {
    Home("Carteira de Cripto ativos", Icons.AutoMirrored.Default.AddToHomeScreen),
    OPERACOES("Operações", Icons.AutoMirrored.Default.List),
    RELATORIOS("Relatórios", Icons.AutoMirrored.Default.Assignment),
    IMPORTACAO("Importar CSV", Icons.AutoMirrored.Default.AddToHomeScreen)
}
