package br.com.nafer.gerenciadorcripto.domain.model

import jakarta.persistence.*

@Entity
@Table(name = "moedas")
data class Moeda(
    @Id
    @Column(name = "ticker", nullable = false, length = 10)
    val ticker: String,

    @Column(name = "nome", nullable = false, length = 100)
    val nome: String? = null,

    @Column(name = "descricao", length = 200)
    val descricao: String? = null,

    @Column(name = "icone", length = 200)
    val icone: String? = null,

    @Column(name = "id_coingecko", length = 200)
    val idCoingecko: String? = null,

    @Column(name = "excluido", nullable = false)
    val excluido: Boolean = false,

    @Column(name = "fiat", nullable = false)
    val fiat: Boolean = false,
)
