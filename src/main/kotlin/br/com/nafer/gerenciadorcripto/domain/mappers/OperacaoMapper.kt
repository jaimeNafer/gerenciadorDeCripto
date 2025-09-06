package br.com.nafer.gerenciadorcripto.domain.mappers

import br.com.nafer.gerenciadorcripto.controllers.dtos.OperacoesResponse
import br.com.nafer.gerenciadorcripto.domain.model.Operacoes
import org.mapstruct.Mapper

@Mapper(componentModel = "spring", uses = [CarteiraMapper::class])
interface OperacaoMapper {
    fun toResponse(entidade: Operacoes): OperacoesResponse
}