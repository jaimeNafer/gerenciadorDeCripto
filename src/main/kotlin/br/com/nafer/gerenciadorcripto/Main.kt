package br.com.nafer.gerenciadorcripto

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import br.com.nafer.gerenciadorcripto.ui.App
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["br.com.nafer.gerenciadorcripto"])
class MainApplication

fun main() {
    System.setProperty("java.awt.headless", "false")
    val context = runApplication<MainApplication>()
    application {
        Window(onCloseRequest = {
            context.close()
            exitApplication()
        }, title = "Consolidador de operações") {
            App(context)
        }
    }
}
