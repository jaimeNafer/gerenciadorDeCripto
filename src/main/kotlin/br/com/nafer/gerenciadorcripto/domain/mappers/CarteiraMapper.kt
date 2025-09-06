package br.com.nafer.gerenciadorcripto.domain.mappers

import br.com.nafer.gerenciadorcripto.controllers.dtos.CarteiraResponse
import br.com.nafer.gerenciadorcripto.controllers.dtos.CarteiraRequest
import br.com.nafer.gerenciadorcripto.domain.model.Carteira
import br.com.nafer.gerenciadorcripto.domain.model.Corretora
import br.com.nafer.gerenciadorcripto.domain.model.Usuario
import br.com.nafer.gerenciadorcripto.domain.model.dtos.CarteiraDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.time.LocalDateTime

@Mapper(componentModel = "spring", uses = [UsuarioMapper::class, CorretoraMapper::class])
interface CarteiraMapper {
    fun toDTO(entidade: Carteira): CarteiraDTO
    fun toResponse(carteira: Carteira): CarteiraResponse
    fun toEntity(carteira: CarteiraRequest): Carteira
}