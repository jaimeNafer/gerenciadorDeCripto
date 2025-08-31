package br.com.nafer.gerenciadorcripto.domain.model.dtos

import br.com.nafer.gerenciadorcripto.domain.model.enums.FinalidadeOperacaoEnum


data class FinalidadesDTO(
    val idFinalidade: Int? = null,
    val nome: FinalidadeOperacaoEnum,
    val descricao: String? = null,
    val codigoExchange: String,
)
