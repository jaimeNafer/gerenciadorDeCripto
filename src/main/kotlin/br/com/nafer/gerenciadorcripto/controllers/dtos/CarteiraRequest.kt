package br.com.nafer.gerenciadorcripto.controllers.dtos

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CarteiraRequest(
    @field:NotBlank(message = "Nome da carteira é obrigatório")
    val nome: String,
    
    @field:NotNull(message = "Usuário é obrigatório")
    @field:Valid
    val usuario: UsuarioRequest,
    
    @field:NotNull(message = "Corretora é obrigatória")
    @field:Valid
    val corretora: CorretoraRequest
)