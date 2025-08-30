package br.com.nafer.gerenciadorcripto.domain.mappers

import br.com.nafer.gerenciadorcripto.domain.model.Carteira
import br.com.nafer.gerenciadorcripto.domain.model.dtos.CarteiraDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring", uses = [UsuarioMapper::class, CorretoraMapper::class])
interface CarteiraMapper {
    fun toDTO(entidade: Carteira): CarteiraDTO
}