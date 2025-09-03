package br.com.nafer.gerenciadorcripto.controllers.dtos

import jakarta.validation.constraints.NotNull

data class UsuarioRequest(
    @field:NotNull(message = "ID do usuário é obrigatório")
    val idUsuario: Int
)