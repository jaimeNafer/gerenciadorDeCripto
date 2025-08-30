package br.com.nafer.gerenciadorcripto.service

import br.com.nafer.gerenciadorcripto.dtos.binance.CorretoraDTO
import br.com.nafer.gerenciadorcripto.infrastructure.repository.CorretoraRepository
import org.springframework.stereotype.Service

@Service
class CorretoraService(val corretoraRepository: CorretoraRepository) {
    fun listarTodasCorretoras(): List<CorretoraDTO> {
        return corretoraRepository.findAll().map { CorretoraDTO(it.idCorretora, it.nome) }
    }
}
