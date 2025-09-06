package br.com.nafer.gerenciadorcripto.controllers

import br.com.nafer.gerenciadorcripto.services.OperacaoService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/ativos")
@CrossOrigin(origins = ["http://localhost:4200"])
@Tag(name = "Ativos", description = "API para gerenciamento de ativos")
class AtivosController(
    private val operacaoService: OperacaoService
) {
}
