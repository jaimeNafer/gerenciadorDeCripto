package br.com.nafer.gerenciadorcripto.services

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.res.loadImageBitmap
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL
import java.security.MessageDigest
import javax.imageio.ImageIO

@Service
class IconeCacheService {
    private val cacheDir = File("cache_icones")
    private val memoryCache = mutableMapOf<String, ImageBitmap?>()
    
    init {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }
    
    /**
     * Gera um nome de arquivo único baseado na URL
     */
    private fun gerarNomeArquivo(url: String): String {
        val hash = MessageDigest.getInstance("MD5")
            .digest(url.toByteArray())
            .joinToString("") { "%02x".format(it) }
        return "$hash.png"
    }
    
    /**
     * Verifica se o ícone existe no cache local
     */
    private fun existeNoCache(url: String): Boolean {
        val arquivo = File(cacheDir, gerarNomeArquivo(url))
        return arquivo.exists()
    }
    
    /**
     * Carrega ícone do cache local
     */
    private fun carregarDoCache(url: String): ImageBitmap? {
        return try {
            val arquivo = File(cacheDir, gerarNomeArquivo(url))
            if (arquivo.exists()) {
                FileInputStream(arquivo).use { inputStream ->
                    loadImageBitmap(inputStream)
                }
            } else null
        } catch (e: Exception) {
            println("Erro ao carregar ícone do cache: ${e.message}")
            null
        }
    }
    
    /**
     * Salva ícone no cache local
     */
    private fun salvarNoCache(url: String, imageBitmap: ImageBitmap) {
        try {
            val arquivo = File(cacheDir, gerarNomeArquivo(url))
            val bufferedImage = imageBitmap.toAwtImage() as BufferedImage
            FileOutputStream(arquivo).use { outputStream ->
                ImageIO.write(bufferedImage, "PNG", outputStream)
            }
        } catch (e: Exception) {
            println("Erro ao salvar ícone no cache: ${e.message}")
        }
    }
    
    /**
     * Baixa ícone da URL
     */
    private suspend fun baixarIcone(url: String): ImageBitmap? {
        return try {
            val bytes = URL(url).readBytes()
            loadImageBitmap(bytes.inputStream())
        } catch (e: Exception) {
            println("Erro ao baixar ícone: ${e.message}")
            null
        }
    }
    
    /**
     * Obtém ícone (do cache de memória, cache local ou baixa da internet)
     */
    suspend fun obterIcone(url: String?): ImageBitmap? {
        if (url == null) return null
        
        // Verifica cache de memória primeiro
        memoryCache[url]?.let { return it }
        
        // Verifica cache local
        if (existeNoCache(url)) {
            val icone = carregarDoCache(url)
            if (icone != null) {
                memoryCache[url] = icone
                return icone
            }
        }
        
        // Baixa da internet e salva no cache
        val icone = baixarIcone(url)
        if (icone != null) {
            salvarNoCache(url, icone)
            memoryCache[url] = icone
        }
        
        return icone
    }
    
    /**
     * Limpa cache de memória
     */
    fun limparCacheMemoria() {
        memoryCache.clear()
    }
    
    /**
     * Limpa cache local (arquivos)
     */
    fun limparCacheLocal() {
        cacheDir.listFiles()?.forEach { it.delete() }
        limparCacheMemoria()
    }
    
    /**
     * Obtém tamanho do cache em MB
     */
    fun obterTamanhoCache(): Double {
        val tamanhoBytes = cacheDir.listFiles()?.sumOf { it.length() } ?: 0L
        return tamanhoBytes / (1024.0 * 1024.0)
    }
}