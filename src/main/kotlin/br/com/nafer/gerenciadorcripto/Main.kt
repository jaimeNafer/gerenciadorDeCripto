package br.com.nafer.gerenciadorcripto

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import br.com.nafer.gerenciadorcripto.ui.MainApp
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["br.com.nafer.gerenciadorcripto"])
@EnableCaching
class MainApplication

fun main() {
    System.setProperty("java.awt.headless", "false")
    val context = runApplication<MainApplication>()
    application {
        val windowState = rememberWindowState()
        
        Window(
            onCloseRequest = {
                context.close()
                exitApplication()
            }, 
            title = "Gerenciador de Cripto",
            state = windowState
        ) {
            MainApp(windowState = windowState, applicationContext = context)
        }
    }
}
