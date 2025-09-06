package br.com.nafer.gerenciadorcripto.controllers.dtos

data class MoedaResponse(
    val ticker: String,
    val nome: String? = null,
    val descricao: String? = null,
    val icone: String? = null,
)
