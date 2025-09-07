package br.com.nafer.gerenciadorcripto.domain.mappers

import br.com.nafer.gerenciadorcripto.controllers.dtos.ArquivoRequest
import br.com.nafer.gerenciadorcripto.controllers.dtos.ArquivoResponse
import br.com.nafer.gerenciadorcripto.controllers.dtos.ArquivoStatusResponse
import br.com.nafer.gerenciadorcripto.domain.model.Arquivo
import br.com.nafer.gerenciadorcripto.dtos.binance.ArquivoDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ArquivoMapper {
    fun toEntity(arquivo: ArquivoRequest): Arquivo
    fun toDTO(arquivo: Arquivo): ArquivoDTO
    fun toResponse(arquivo: Arquivo): ArquivoResponse
    fun toStatusResponse(arquivo: Arquivo): ArquivoStatusResponse
}