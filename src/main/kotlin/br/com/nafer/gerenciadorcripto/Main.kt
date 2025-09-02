package br.com.nafer.gerenciadorcripto

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.CrossOrigin

@SpringBootApplication
@ComponentScan(basePackages = ["br.com.nafer.gerenciadorcripto"])
@EnableCaching
@CrossOrigin(origins = ["http://localhost:4200"])
class MainApplication

fun main(args: Array<String>) {
    runApplication<MainApplication>(*args)
}
