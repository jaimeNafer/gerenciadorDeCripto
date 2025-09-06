package br.com.nafer.gerenciadorcripto.domain.model

import br.com.nafer.gerenciadorcripto.domain.model.enums.StatusOperacaoEnum
import br.com.nafer.gerenciadorcripto.domain.model.enums.TipoOperacaoEnum
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "operacoes")
data class Operacoes(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operacao")
    val idOperacao: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_finalidade", nullable = false)
    val finalidade: Finalidades,

    @ManyToOne
    @JoinColumn(name = "id_arquivo")
    val arquivo: Arquivo? = null,

    @Column(name = "data_operacao_entrada")
    val dataOperacaoEntrada: LocalDateTime?,

    @ManyToOne
    @JoinColumn(name = "moeda_entrada", referencedColumnName = "ticker")
    val moedaEntrada: Moeda?,

    @Column(name = "quantidade_entrada", precision = 19, scale = 4)
    val quantidadeEntrada: BigDecimal?,

    @Column(name = "data_operacao_saida")
    val dataOperacaoSaida: LocalDateTime?,

    @ManyToOne
    @JoinColumn(name = "moeda_saida", referencedColumnName = "ticker")
    val moedaSaida: Moeda?,

    @Column(name = "quantidade_saida", precision = 19, scale = 4)
    val quantidadeSaida: BigDecimal?,

    @Column(name = "valor_brl", precision = 19, scale = 4)
    var valorBrl: BigDecimal? = null,

    @Column(name = "lucro_prejuizo", precision = 19, scale = 4)
    var lucroPrejuizo: BigDecimal? = null,

    @Column(name = "destino", length = 100)
    val destino: String? = null,

    @Column(name = "taxa_quantidade", precision = 19, scale = 4)
    val taxaQuantidade: BigDecimal? = null,

    @Column(name = "taxa_moeda", length = 10)
    val taxaMoeda: String? = null,

    @Column(name = "taxa_valor_brl", precision = 19, scale = 4)
    val taxaValorBrl: BigDecimal? = null,

    @Column(name = "data_criacao", nullable = false)
    val dataCriacao: LocalDateTime = LocalDateTime.now(),

    @Column(name = "excluido", nullable = false)
    val excluido: Boolean = false,

    @Column(name = "origem_registro", length = 20)
    val origemRegistro: String,

    @Column(name = "observacao", length = 500)
    val observacao: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacao", nullable = false, length = 45)
    val tipoOperacao: TipoOperacaoEnum,

    @Enumerated(EnumType.STRING)
    @Column(name = "status_operacao", nullable = false)
    var statusOperacao: StatusOperacaoEnum = StatusOperacaoEnum.PENDENTE,
)
