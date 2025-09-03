package br.com.nafer.gerenciadorcripto.controllers.dtos

data class CarteiraResponse(
    val idCarteira: Int?,
    val nome: String,
    val excluido: Boolean,
    val usuario:  UsuarioResponse,
    val corretora: CorretoraResponse,
)