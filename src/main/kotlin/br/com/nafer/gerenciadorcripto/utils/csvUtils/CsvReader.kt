package br.com.nafer.gerenciadorcripto.utils.csvUtils

import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

fun <T> parseCsvFromMultipart(
    conteudo: ByteArray,
    clazz: Class<T>,
    separador: Char = ';',
    charset: Charset = Charsets.UTF_8
): List<T> {
    InputStreamReader(ByteArrayInputStream(conteudo), charset).use { reader ->
        val csvToBean: CsvToBean<T> = CsvToBeanBuilder<T>(reader)
            .withType(clazz)
            .withSeparator(separador)
            .withIgnoreLeadingWhiteSpace(true)
            .build()
        return csvToBean.parse()
    }
}
