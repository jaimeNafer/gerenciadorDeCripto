package br.com.nafer.gerenciadorcripto.controllers

import br.com.nafer.gerenciadorcripto.domain.model.dtos.OperacoesDTO
import br.com.nafer.gerenciadorcripto.service.OperacaoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/ativos")
@CrossOrigin(origins = ["http://localhost:4200"])
@Tag(name = "Ativos", description = "Opera��es relacionadas aos ativos de criptomoedas")
class AtivosController(
    private val operacaoService: OperacaoService
) {

    @GetMapping
    @Operation(
        summary = "Listar todos os ativos",
        description = "Retorna uma lista com todas as opera��es de ativos de criptomoedas cadastradas no sistema"
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Lista de ativos retornada com sucesso",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = OperacoesDTO::class)
            )]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = [Content()]
        )
    ])
    fun listarAtivos(): ResponseEntity<List<OperacoesDTO>> {
        return try {
            val ativos = operacaoService.listarOperacoes()
            ResponseEntity.ok(ativos)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/health")
    @Operation(
        summary = "Verificar sa�de do servi�o",
        description = "Endpoint para verificar se o servi�o de ativos est� funcionando corretamente"
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Servi�o funcionando corretamente",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(type = "object")
            )]
        )
    ])
    fun health(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("status" to "UP", "service" to "AtivosController"))
    }
}
