package br.com.nafer.gerenciadorcripto.services

import br.com.nafer.gerenciadorcripto.dtos.binance.UsuarioDTO
import br.com.nafer.gerenciadorcripto.infrastructure.repository.UsuarioRepository
import org.springframework.stereotype.Service

@Service
class UsuarioService(val usuarioRepository: UsuarioRepository) {
    fun listarTodosUsuarios(): List<UsuarioDTO> {
        return usuarioRepository.findAll().map {user-> UsuarioDTO(user.idUsuario, user.nome) }
    }
}
