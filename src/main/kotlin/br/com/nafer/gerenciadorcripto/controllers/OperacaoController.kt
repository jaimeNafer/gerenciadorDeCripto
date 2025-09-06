package br.com.nafer.gerenciadorcripto.controllers

import br.com.nafer.gerenciadorcripto.controllers.dtos.ArquivoRequest
import br.com.nafer.gerenciadorcripto.controllers.dtos.ArquivoResponse
import br.com.nafer.gerenciadorcripto.controllers.dtos.OperacoesResponse
import br.com.nafer.gerenciadorcripto.services.ArquivoService
import br.com.nafer.gerenciadorcripto.services.OperacaoService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URI

@RestController
@RequestMapping("/v1/carteiras/{idCarteira}/operacoes")
@CrossOrigin(origins = ["http://localhost:4200"])
@Tag(name = "Operações", description = "API para gerenciamento de operações")
class OperacaoController(
    private val operacaoService: OperacaoService
) {
    @GetMapping
    fun listarOperacoes(@PathVariable idCarteira: Int): ResponseEntity<List<OperacoesResponse>> {
        return ResponseEntity(operacaoService.listarOperacoes(idCarteira), HttpStatus.OK)
    }
}
