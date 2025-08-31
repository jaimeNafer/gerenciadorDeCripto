package br.com.nafer.gerenciadorcripto.ui.viewmodel

import br.com.nafer.gerenciadorcripto.dtos.binance.ArquivoDTO
import br.com.nafer.gerenciadorcripto.service.ArquivoService
import org.springframework.stereotype.Component

@Component
class ArquivoViewModel(private val arquivoService: ArquivoService) {
    fun listar(): List<ArquivoDTO> {
        return arquivoService.listar()
    }
}