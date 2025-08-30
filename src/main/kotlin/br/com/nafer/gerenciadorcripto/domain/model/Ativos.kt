package br.com.nafer.gerenciadorcripto.domain.model

import jakarta.persistence.*
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "ativos",)
data class Ativos(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ativo")
    val idAtivo: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_carteira", nullable = false)
    val carteira: Carteira,

    @ManyToOne
    @JoinColumn(name = "moeda", referencedColumnName = "ticker")
    val moeda: Moeda,

    @Column(precision = 38, scale = 18, nullable = false)
    val quantidade: BigDecimal = BigDecimal.ZERO,

    @Column(name = "preco_medio", precision = 38, scale = 18, nullable = false)
    val precoMedio: BigDecimal = BigDecimal.ZERO,

    @Column(name = "valor_investido", precision = 38, scale = 18, nullable = false)
    val valorInvestido: BigDecimal = BigDecimal.ZERO,

    @Column(name = "lucro_prejuizo", precision = 38, scale = 18, nullable = false)
    val lucroPrejuizo: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    val excluido: Boolean = false,

    @Column(name = "data_criacao")
    val dataCriacao: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "data_atualizacao")
    val dataAtualizacao: LocalDateTime? = null,

    @Column(name = "data_exclusao")
    val dataExclusao: LocalDateTime? = null,

    @Column(name = "moeda_base", length = 10)
    val moedaBase: String = "BRL"
): Serializable
