package br.com.nafer.gerenciadorcripto.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.io.Serializable

@Entity
@Table(name = "usuarios")
data class Usuario(

    @Id
    @Column(name = "id_usuario")
    val idUsuario: Int? = null,

    @Column(name = "nome", nullable = false, length = 200)
    val nome: String,

    @Column(name = "cpf_cnpj", nullable = false, length = 14)
    val cpfCnpj: String,

    @Column(name = "excluido", nullable = false)
    val excluido: Boolean = false
): Serializable
