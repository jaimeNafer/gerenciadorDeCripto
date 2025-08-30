package br.com.nafer.gerenciadorcripto.domain.model.dtos

import br.com.nafer.gerenciadorcripto.domain.model.enums.FinalidadeOperacaoEnum
import br.com.nafer.gerenciadorcripto.domain.model.enums.TipoOperacaoEnum
import br.com.nafer.gerenciadorcripto.dtos.binance.CsvBinanceDTO

data class RegistroEmParesDTO(val entrada: CsvBinanceDTO?, val saida: CsvBinanceDTO?, val finalidade: FinalidadeOperacaoEnum, val tipoOperacao: TipoOperacaoEnum)
