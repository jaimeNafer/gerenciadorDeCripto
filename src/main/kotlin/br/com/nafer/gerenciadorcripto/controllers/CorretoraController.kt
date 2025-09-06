package br.com.nafer.gerenciadorcripto.controllers

import br.com.nafer.gerenciadorcripto.controllers.dtos.CorretoraResponse
import br.com.nafer.gerenciadorcripto.services.CorretoraService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/corretoras")
@CrossOrigin(origins = ["http://localhost:4200"])
@Tag(name = "Corretoras", description = "API para gerenciamento de corretoras")
class CorretoraController(
    private val corretoraService: CorretoraService
) {
    
    @GetMapping
    @Operation(summary = "Listar todas as corretoras", description = "Retorna uma lista com todas as corretoras disponíveis")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de corretoras retornada com sucesso"),
        ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    ])
    fun listarCorretoras(): ResponseEntity<List<CorretoraResponse>> {
        val corretoras = corretoraService.listarTodasCorretoras()
        return ResponseEntity.ok(corretoras)
    }
}