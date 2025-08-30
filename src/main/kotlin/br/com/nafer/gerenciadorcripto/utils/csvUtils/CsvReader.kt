package br.com.nafer.gerenciadorcripto.utils.csvUtils

import br.com.nafer.gerenciadorcripto.domain.model.Arquivo
import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import br.com.nafer.gerenciadorcripto.dtos.binance.ArquivoDTO
import java.io.FileReader
import java.io.IOException

fun <T> carregar(arquivo: ArquivoDTO, classeT: Class<T>): List<T> {
    try {
        FileReader(arquivo.caminho).use { reader ->
            val csvToBean: CsvToBean<T> = CsvToBeanBuilder<T>(reader)
                .withSeparator(';')
                .withType(classeT)
                .withIgnoreLeadingWhiteSpace(true)
                .build()
            return csvToBean.parse()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        throw RuntimeException("Erro ao ler o CSV: " + e.message)
    }
}
