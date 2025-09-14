package br.com.nafer.gerenciadorcripto.services.events

data class ArquivoCriadoEvent(
    val idCarteira: Int,
    val idArquivo: Int,
    val nome: String,
    val conteudo: ByteArray,
    val hashArquivo: String
)