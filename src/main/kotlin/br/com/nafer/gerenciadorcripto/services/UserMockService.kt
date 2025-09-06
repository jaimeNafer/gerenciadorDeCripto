package br.com.nafer.gerenciadorcripto.services

import br.com.nafer.gerenciadorcripto.domain.model.Usuario
import br.com.nafer.gerenciadorcripto.infrastructure.repository.UsuarioRepository
import org.springframework.stereotype.Service

@Service
class UserMockService(private val usuarioRepository: UsuarioRepository) {
    
    /**
     * Retorna sempre o usuário com ID 1 como usuário logado no sistema.
     * Este é um mock temporário até a implementação da segurança JWT.
     */
    fun getUsuarioLogado(): Usuario {
        return usuarioRepository.findById(1)
            .orElseThrow { RuntimeException("Usuário padrão com ID 1 não encontrado no sistema") }
    }
}