package br.com.nafer.gerenciadorcripto.controllers.dtos

import jakarta.validation.constraints.NotNull

data class CorretoraRequest(
    @field:NotNull(message = "ID da corretora é obrigatório")
    val idCorretora: Int
)