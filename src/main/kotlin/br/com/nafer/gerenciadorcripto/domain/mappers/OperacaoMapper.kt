package br.com.nafer.gerenciadorcripto.domain.mappers

import br.com.nafer.gerenciadorcripto.domain.model.Operacoes
import br.com.nafer.gerenciadorcripto.domain.model.dtos.OperacoesDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring", uses = [CarteiraMapper::class])
interface OperacaoMapper {
    fun toDTO(entidade: Operacoes): OperacoesDTO
}