package br.com.nafer.gerenciadorcripto.domain.model

import jakarta.persistence.*

@Entity
@Table(name = "usuarios_corretoras")
data class UsuarioCorretora(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_corretora")
    val idUsuarioCorretora: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    val usuario: Usuario,

    @ManyToOne
    @JoinColumn(name = "id_corretora", nullable = false)
    val corretora: Corretora,

    @Column(name = "excluido", nullable = false)
    val excluido: Boolean = false
)
