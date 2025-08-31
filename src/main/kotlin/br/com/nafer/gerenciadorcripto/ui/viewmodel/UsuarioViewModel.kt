package br.com.nafer.gerenciadorcripto.ui.viewmodel

import br.com.nafer.gerenciadorcripto.dtos.binance.UsuarioDTO
import br.com.nafer.gerenciadorcripto.service.UsuarioService
import org.springframework.stereotype.Component

@Component
class UsuarioViewModel(private val usuarioService: UsuarioService) {
    fun listarTodosUsuarios(): List<UsuarioDTO> {
        return usuarioService.listarTodosUsuarios()
    }
}