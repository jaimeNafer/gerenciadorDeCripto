package br.com.nafer.gerenciadorcripto.domain.model.dtos

import br.com.nafer.gerenciadorcripto.dtos.binance.UsuarioDTO
import br.com.nafer.gerenciadorcripto.dtos.binance.CorretoraDTO

data class CarteiraDTO(
    val idCarteira: Int?,
    val usuario: UsuarioDTO,
    val corretora: CorretoraDTO
)