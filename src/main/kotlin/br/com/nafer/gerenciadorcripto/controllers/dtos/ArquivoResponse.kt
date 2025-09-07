package br.com.nafer.gerenciadorcripto.controllers.dtos

import br.com.nafer.gerenciadorcripto.domain.model.enums.StatusArquivoEnum

data class ArquivoResponse(val idArquivo: Int, val nome: String, val status: StatusArquivoEnum, val tamanhoBytes: Long, val carteira: CarteiraResumidaResponse)

data class CarteiraResumidaResponse(val idCarteira: Int, val nome: String)