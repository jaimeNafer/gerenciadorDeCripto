package br.com.nafer.gerenciadorcripto.dtos.binance

data class ArquivoDTO(val idArquivo: Int, val nome: String, var caminho: String? = null, val conteudo: ByteArray? = null, val separador: Char = ';')
