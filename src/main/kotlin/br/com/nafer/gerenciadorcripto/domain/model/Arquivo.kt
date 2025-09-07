package br.com.nafer.gerenciadorcripto.domain.model

import br.com.nafer.gerenciadorcripto.domain.model.enums.StatusArquivoEnum
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
    @JoinColumn(name = "id_carteira", nullable = false)
    val carteira: Carteira,

    @Column(name = "nome", nullable = false, length = 200)
    val nome: String,

    @Column(name = "hash_arquivo", nullable = false, length = 45, unique = true)
    val hashArquivo: String,

    @Column(name = "storage_key")
    val storageKey: String? = null,

    @Column(name = "data_criacao")
    val dataCriacao: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    val status: StatusArquivoEnum = StatusArquivoEnum.PENDENTE,

    @Column(name = "tamanho_bytes")
    val tamanhoBytes: Long

    )
