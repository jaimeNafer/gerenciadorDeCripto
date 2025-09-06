package br.com.nafer.gerenciadorcripto.controllers.dtos

data class ArquivoResponse(val idArquivo: Int, val nome: String, val carteira: CarteiraResumidaResponse)

data class CarteiraResumidaResponse(val idCarteira: Int, val nome: String)