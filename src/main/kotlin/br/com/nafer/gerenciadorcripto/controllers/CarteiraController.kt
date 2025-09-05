package br.com.nafer.gerenciadorcripto.controllers

import br.com.nafer.gerenciadorcripto.controllers.dtos.CarteiraRequest
import br.com.nafer.gerenciadorcripto.controllers.dtos.CarteiraResponse
import br.com.nafer.gerenciadorcripto.service.CarteiraService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/carteiras")
@CrossOrigin(origins = ["http://localhost:4200"])
@Tag(name = "Carteiras", description = "API para gerenciamento de carteiras")
class CarteiraController(
    private val carteiraService: CarteiraService
) {
    @GetMapping
    @Operation(summary = "Listar todas as carteiras", description = "Retorna uma lista com todas as carteiras do usuário")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de carteiras retornada com sucesso"),
        ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    ])
    fun listarCarteiras(): ResponseEntity<List<CarteiraResponse>> {
        return ResponseEntity(carteiraService.obterCarteiras(), HttpStatus.OK)
    }

    @PostMapping
    @Operation(summary = "Criar nova carteira", description = "Cria uma nova carteira para o usuário")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Carteira criada com sucesso"),
        ApiResponse(responseCode = "400", description = "Dados inválidos"),
        ApiResponse(responseCode = "422", description = "Carteira já existe ou usuário já possui carteira na corretora")
    ])
    fun criarCarteira(@Valid @RequestBody request: CarteiraRequest): ResponseEntity<HttpStatus> {
        val carteira = carteiraService.criarCarteira(request)
        return ResponseEntity.ok(HttpStatus.CREATED)
    }
}
