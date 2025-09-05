package br.com.nafer.gerenciadorcripto.domain.mappers

import br.com.nafer.gerenciadorcripto.controllers.dtos.CorretoraResponse
import br.com.nafer.gerenciadorcripto.domain.model.Corretora
import br.com.nafer.gerenciadorcripto.dtos.binance.CorretoraDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface CorretoraMapper {

    fun toEntity(corretora: CorretoraDTO): Corretora
    
    fun toDTO(corretora: Corretora): CorretoraDTO
    
    fun toResponse(corretora: Corretora): CorretoraResponse
}