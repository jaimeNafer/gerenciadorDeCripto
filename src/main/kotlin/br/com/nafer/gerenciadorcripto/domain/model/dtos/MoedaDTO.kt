package br.com.nafer.gerenciadorcripto.domain.model.dtos

data class MoedaDTO(
    val ticker: String,
    val nome: String? = null,
    val descricao: String? = null,
    val icone: String? = null,
)
