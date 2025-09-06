package br.com.nafer.gerenciadorcripto.services

import br.com.nafer.gerenciadorcripto.controllers.dtos.CarteiraRequest
import br.com.nafer.gerenciadorcripto.controllers.dtos.CarteiraResponse
import br.com.nafer.gerenciadorcripto.domain.mappers.CarteiraMapper
import br.com.nafer.gerenciadorcripto.domain.model.*
import br.com.nafer.gerenciadorcripto.exceptions.CarteiraJaExisteException
import br.com.nafer.gerenciadorcripto.exceptions.NotFoundException
import br.com.nafer.gerenciadorcripto.exceptions.UsuarioJaPossuiCarteiraException
import br.com.nafer.gerenciadorcripto.infrastructure.repository.*
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Service
class CarteiraService(
    private val carteiraRepository: CarteiraRepository,
    private val moedaService: MoedaService,
    private val ativosRepository: AtivosRepository,
    private val usuarioRepository: UsuarioRepository,
    private val corretoraService: CorretoraService,
    private val userMockService: UserMockService,
    private val mapper: CarteiraMapper
) {

    fun obterCarteiraOu404(idCarteira: Int): Carteira {
        return carteiraRepository.findById(idCarteira).orElseThrow { NotFoundException("Carteira: $idCarteira não encontrada") }
    }
    fun obterCarteiras(): List<CarteiraResponse> {
        return carteiraRepository.findAll()
            .map { mapper.toResponse(it) }
            .sortedBy { it.nome }
    }

    fun criarCarteira(request: CarteiraRequest): Carteira {

        val usuario = userMockService.getUsuarioLogado()
        val idCorretora = request.corretora.idCorretora
        val corretora = corretoraService.obterCorretora(idCorretora)
        val carteira = Carteira(nome = request.nome, usuario = usuario, corretora = corretora)

        if (carteiraRepository.existsByNome(carteira.nome)) {
            throw CarteiraJaExisteException(carteira.nome)
        }
        
        if (carteiraRepository.existsByUsuarioAndCorretora(usuario, corretora)) {
            throw UsuarioJaPossuiCarteiraException(corretora.nome)
        }
        
        return carteiraRepository.save(carteira.copy(usuario = usuario, corretora = corretora))
    }

    fun obterCarteirasPorUsuarioECorretora(usuario: List<Usuario>? = null, corretora: List<Corretora>? = null): List<Carteira>? {
        return when {
            usuario?.isNotEmpty() == true && corretora?.isNotEmpty() == true -> carteiraRepository.filtrarCarteiraPorUsuariosECorretoras(
                usuario,
                corretora
            )

            usuario?.isNotEmpty() == true -> carteiraRepository.filtrarAtivosPorUsuarios(usuario)
            corretora?.isNotEmpty() == true -> carteiraRepository.filtrarAtivosPorCorretoras(corretora)
            else -> carteiraRepository.findAll()
        }
    }

    fun atualizarAtivo(
        carteira: Carteira,
        ticker: String,
        quantidadeTotal: BigDecimal,
        valorTotal: BigDecimal,
        precoMedioTotal: BigDecimal,
        lucroPrejuizoTotal: BigDecimal,
    ) {
        val moeda = moedaService.obterMoedaPorTicker(ticker)

        val ativo = ativosRepository.findByCarteiraAndMoeda(carteira, moeda)?.let {
            it.copy(
                quantidade = quantidadeTotal,
                valorInvestido = valorTotal,
                precoMedio = precoMedioTotal,
                lucroPrejuizo = BigDecimal.ZERO,
            )
        } ?: Ativos(
            carteira = carteira,
            moeda = moeda,
            quantidade = quantidadeTotal,
            valorInvestido = valorTotal,
            precoMedio = precoMedioTotal,
            lucroPrejuizo = lucroPrejuizoTotal,
            dataAtualizacao = LocalDateTime.now(),
        )

        ativosRepository.save(ativo)
    }

    private fun criarCarteira(usuario: Usuario, corretora: Corretora): Carteira {
        val complementoRandomico = UUID.randomUUID()
        val nome = "${usuario.nome.split(" ")[0]}_${corretora.nome.split(" ")[0]}_$complementoRandomico"
        return carteiraRepository.save(Carteira(nome = nome, usuario = usuario, corretora = corretora))
    }

    fun obterAtivosPorCarteira(carteira: Carteira): List<Ativos> {
        return ativosRepository.findByCarteira(carteira)
    }

    private fun criarAtivo(carteira: Carteira) {

    }

    fun removerAtivos(carteira: Carteira, tickers: List<String>) {
        ativosRepository.deleteAllByCarteiraAndMoedaTickerIn(carteira, tickers)
    }
}