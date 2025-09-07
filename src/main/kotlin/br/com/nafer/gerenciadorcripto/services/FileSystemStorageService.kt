package br.com.nafer.gerenciadorcripto.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.util.zip.GZIPOutputStream

@Service
class FileSystemStorageService(
    @Value("\${app.storage.root}") private val rootDir: String
) {
    private val root = Paths.get(rootDir).toAbsolutePath().normalize()

    fun saveCsvGz(
        idCarteira: Int,
        idArquivo: Int,
        nome: String,
        conteudo: ByteArray,
        hashArquivo: String
    ): String {
        val now = LocalDate.now()
        val dir = root
            .resolve("carteiras")
            .resolve(idCarteira.toString())
            .resolve("%04d".format(now.year))
            .resolve("%02d".format(now.monthValue))

        Files.createDirectories(dir)

        val filename = "$idArquivo-$hashArquivo.csv.gz"

        val path = dir.resolve(filename)
        GZIPOutputStream(Files.newOutputStream(path)).use { gz ->
            gz.write(conteudo)
        }
        return root.relativize(path).toString().replace('\\', '/')
    }

    fun deleteByKey(storageKey: String) {
        val path = root.resolve(storageKey).normalize()
        Files.deleteIfExists(path)
    }

    fun loadAsResource(storageKey: String): Resource {
        val path = root.resolve(storageKey).normalize()
        return FileSystemResource(path)
    }
}
