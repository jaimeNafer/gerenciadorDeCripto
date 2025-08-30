package br.com.nafer.gerenciadorcripto.domain.mappers

import br.com.nafer.gerenciadorcripto.domain.model.Operacoes
import br.com.nafer.gerenciadorcripto.domain.model.dtos.OperacoesDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface OperacaoMapper {
    fun toDTO(entidade: Operacoes): OperacoesDTO
}