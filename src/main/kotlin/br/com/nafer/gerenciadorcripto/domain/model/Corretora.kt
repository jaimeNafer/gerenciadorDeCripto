package br.com.nafer.gerenciadorcripto.domain.model

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "corretoras")
data class Corretora(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_corretora")
    val idCorretora: Int? = null,

    @Column(name = "nome", nullable = false, length = 100)
    val nome: String,

    @Column(name = "cnpj", nullable = false, length = 14)
    val cnpj: String,

    @Column(name = "site", length = 300)
    val site: String? = null,

    @Column(name = "endereco", length = 300)
    val endereco: String? = null,

    @Column(name = "exchange_nacional", nullable = false)
    val exchange_nacional: Boolean = false,

    @Column(name = "excluido", nullable = false)
    val excluido: Boolean = false,
): Serializable
