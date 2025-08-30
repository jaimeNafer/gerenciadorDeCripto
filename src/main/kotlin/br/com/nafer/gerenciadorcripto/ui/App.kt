package br.com.nafer.gerenciadorcripto.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.nafer.gerenciadorcripto.ui.components.AppDrawer
import br.com.nafer.gerenciadorcripto.ui.components.AppTopBar
import br.com.nafer.gerenciadorcripto.navigation.Screen
import br.com.nafer.gerenciadorcripto.ui.screens.*
import org.springframework.context.ConfigurableApplicationContext

@Composable
fun App(applicationContext: ConfigurableApplicationContext) {
    var currentScreen by remember { mutableStateOf(Screen.OPERACOES) }

    MaterialTheme {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxHeight().width(250.dp)){
                AppDrawer(
                    currentScreen = currentScreen,
                    onDestinationClicked = { screen -> currentScreen = screen }
                )
            }
            Column(modifier = Modifier.weight(1f).fillMaxHeight()){
                AppTopBar(title = currentScreen.title)
                Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    when (currentScreen) {
                        Screen.Home -> TelaDeCarteiraPreview(applicationContext)
                        Screen.IMPORTACAO -> telaDeImportacaoCsv(applicationContext)
                        Screen.OPERACOES -> telaDeOperacoes(applicationContext)
                        Screen.RELATORIOS -> homeScreen()
                    }
                }

            }
        }
    }
}
