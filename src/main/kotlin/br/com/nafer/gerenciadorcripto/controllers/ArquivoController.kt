package br.com.nafer.gerenciadorcripto.controllers

import br.com.nafer.gerenciadorcripto.controllers.dtos.ArquivoRequest
import br.com.nafer.gerenciadorcripto.controllers.dtos.ArquivoResponse
import br.com.nafer.gerenciadorcripto.services.ArquivoService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URI

@RestController
@RequestMapping("/v1/carteiras/{idCarteira}/arquivos")
@CrossOrigin(origins = ["http://localhost:4200"])
@Tag(name = "Arquivos", description = "API para gerenciamento de arquivos")
class ArquivoController(
    private val arquivoService: ArquivoService
) {
    @GetMapping
    fun listarArquivos(@PathVariable idCarteira: Int): ResponseEntity<List<ArquivoResponse>> {
        return ResponseEntity(arquivoService.obterArquivosPorIdCarteira(idCarteira), HttpStatus.OK)
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadArquivoCsv(
        @PathVariable idCarteira: Int,
        @RequestPart("file") file: MultipartFile,
        @RequestParam(name = "separador", defaultValue = ";") separador: String
    ): ResponseEntity<HttpStatus> {
        arquivoService.salvar(idCarteira, file, separador)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/{idArquivo}")
    fun deletarArquivo(@PathVariable idCarteira: Int, @PathVariable idArquivo: Int): ResponseEntity<HttpStatus> {
        arquivoService.processarExclusaoArquivo(idCarteira, idArquivo)
        return ResponseEntity.ok(HttpStatus.NO_CONTENT)
    }
}
