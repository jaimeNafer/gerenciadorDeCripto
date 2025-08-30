package br.com.nafer.gerenciadorcripto.domain.model

import jakarta.persistence.*
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "carteira",)
data class Carteira(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carteira")
    val idCarteira: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    val usuario: Usuario,

    @ManyToOne
    @JoinColumn(name = "id_corretora", referencedColumnName = "id_corretora")
    val corretora: Corretora,

    @Column(nullable = false)
    val excluido: Boolean = false,

    @Column(name = "data_criacao")
    val dataCriacao: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "data_atualizacao")
    val dataAtualizacao: LocalDateTime? = null,

    @Column(name = "data_exclusao")
    val dataExclusao: LocalDateTime? = null,
): Serializable
