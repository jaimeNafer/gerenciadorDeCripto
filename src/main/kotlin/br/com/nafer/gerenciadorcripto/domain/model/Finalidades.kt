package br.com.nafer.gerenciadorcripto.domain.model

import jakarta.persistence.*
import br.com.nafer.gerenciadorcripto.domain.model.enums.FinalidadeOperacaoEnum

@Entity
@Table(name = "finalidades")
data class Finalidades(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_finalidade")
    val idFinalidade: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_corretora", nullable = false)
    val corretora: Corretora,

    @Enumerated(EnumType.STRING)
    @Column(name = "nome", nullable = false, length = 45)
    val nome: FinalidadeOperacaoEnum,

    @Column(name = "descricao", length = 100)
    val descricao: String? = null,

    @Column(name = "codigo_exchange", nullable = false, length = 50)
    val codigoExchange: String,

    @Column(name = "excluido", nullable = false)
    val excluido: Boolean = false
)
