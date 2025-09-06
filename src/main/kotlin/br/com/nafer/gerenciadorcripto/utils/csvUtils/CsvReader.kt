package br.com.nafer.gerenciadorcripto.utils.csvUtils

import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import org.springframework.web.multipart.MultipartFile
import java.nio.charset.Charset

fun <T> parseCsvFromMultipart(
    file: MultipartFile,
    clazz: Class<T>,
    separador: Char = ';',
    charset: Charset = Charsets.UTF_8
): List<T> {
    file.inputStream.reader(charset).use { reader ->
        val csvToBean: CsvToBean<T> = CsvToBeanBuilder<T>(reader)
            .withType(clazz)
            .withSeparator(separador)
            .withIgnoreLeadingWhiteSpace(true)
            .build()
        return csvToBean.parse()
    }
}
