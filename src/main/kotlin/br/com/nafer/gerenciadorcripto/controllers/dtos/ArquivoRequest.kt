package br.com.nafer.gerenciadorcripto.controllers.dtos

data class ArquivoRequest(val nome: String, var caminho: String? = null, val conteudo: ByteArray? = null, val separador: Char = ';')
