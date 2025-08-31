package br.com.nafer.gerenciadorcripto.ui.viewmodel

import br.com.nafer.gerenciadorcripto.dtos.binance.CorretoraDTO
import br.com.nafer.gerenciadorcripto.service.CorretoraService
import org.springframework.stereotype.Component

@Component
class CorretoraViewModel(private val corretoraService: CorretoraService) {
    fun listarTodasCorretoras(): List<CorretoraDTO> {
        return corretoraService.listarTodasCorretoras()
    }
}