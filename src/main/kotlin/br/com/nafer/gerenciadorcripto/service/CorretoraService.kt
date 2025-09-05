package br.com.nafer.gerenciadorcripto.service

import br.com.nafer.gerenciadorcripto.controllers.dtos.CorretoraResponse
import br.com.nafer.gerenciadorcripto.domain.mappers.CorretoraMapper
import br.com.nafer.gerenciadorcripto.infrastructure.repository.CorretoraRepository
import org.springframework.stereotype.Service

@Service
class CorretoraService(
    val corretoraRepository: CorretoraRepository,
    val corretoraMapper: CorretoraMapper
) {
    fun listarTodasCorretoras(): List<CorretoraResponse> {
        return corretoraRepository.findAll().map { corretoraMapper.toResponse(it) }
    }
}
