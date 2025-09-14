package br.com.nafer.gerenciadorcripto.controllers.dtos

import br.com.nafer.gerenciadorcripto.domain.model.enums.FinalidadeOperacaoEnum


data class FinalidadesResponse(
    val nome: FinalidadeOperacaoEnum,
    val descricao: String? = null,
)
