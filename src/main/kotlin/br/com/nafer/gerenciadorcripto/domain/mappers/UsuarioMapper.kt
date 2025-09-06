package br.com.nafer.gerenciadorcripto.domain.mappers

import br.com.nafer.gerenciadorcripto.domain.model.Usuario
import br.com.nafer.gerenciadorcripto.dtos.binance.UsuarioDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface UsuarioMapper {

    fun toEntity(usuario: UsuarioDTO): Usuario
    fun toDTO(usuario: Usuario): UsuarioDTO
}