package br.com.nafer.gerenciadorcripto.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "arquivos")
data class Arquivo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_arquivo")
    val idArquivo: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    val usuario: Usuario,

    @ManyToOne
    @JoinColumn(name = "id_corretora", nullable = false)
    val corretora: Corretora,

    @Column(name = "nome", nullable = false, length = 200)
    val nome: String,

    @Column(name = "hash_arquivo", nullable = false, length = 45, unique = true)
    val hashArquivo: String,

    @Column(name = "caminho")
    val caminho: String,

    @Column(name = "data_criacao")
    val dataCriacao: LocalDateTime = LocalDateTime.now(),
)
