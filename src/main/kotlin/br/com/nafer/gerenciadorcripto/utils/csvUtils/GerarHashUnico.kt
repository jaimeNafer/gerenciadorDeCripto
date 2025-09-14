package br.com.nafer.gerenciadorcripto.utils.csvUtils

import java.security.MessageDigest

fun gerarHashUnico(conteudo: ByteArray?): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(conteudo)
    return hashBytes.joinToString("") { "%02x".format(it) }
}
