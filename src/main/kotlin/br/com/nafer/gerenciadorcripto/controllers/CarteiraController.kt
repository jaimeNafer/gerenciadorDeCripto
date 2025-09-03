package br.com.nafer.gerenciadorcripto.controllers

import br.com.nafer.gerenciadorcripto.controllers.dtos.CarteiraResponse
import br.com.nafer.gerenciadorcripto.controllers.dtos.CarteiraRequest
import br.com.nafer.gerenciadorcripto.service.CarteiraService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/carteiras")
@CrossOrigin(origins = ["http://localhost:4200"])
class CarteiraController(
    private val carteiraService: CarteiraService
) {
    @GetMapping
    fun listarCarteiras(): ResponseEntity<List<CarteiraResponse>> {
        return ResponseEntity(carteiraService.obterCarteiras(), HttpStatus.OK)
    }

    @PostMapping
    fun criarCarteira(@Valid @RequestBody request: CarteiraRequest): ResponseEntity<Void> {
        carteiraService.criarCarteira(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}
