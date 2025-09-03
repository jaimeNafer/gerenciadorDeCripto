package br.com.nafer.gerenciadorcripto.service

import br.com.nafer.gerenciadorcripto.controllers.dtos.CarteiraResponse
import br.com.nafer.gerenciadorcripto.controllers.dtos.CarteiraRequest
import br.com.nafer.gerenciadorcripto.domain.mappers.CarteiraMapper
import br.com.nafer.gerenciadorcripto.domain.model.Ativos
import br.com.nafer.gerenciadorcripto.domain.model.Carteira
import br.com.nafer.gerenciadorcripto.domain.model.Corretora
import br.com.nafer.gerenciadorcripto.infrastructure.repository.CarteiraRepository
import br.com.nafer.gerenciadorcripto.domain.model.Usuario
import br.com.nafer.gerenciadorcripto.exceptions.NotFoundException
import br.com.nafer.gerenciadorcripto.infrastructure.repository.AtivosRepository
import br.com.nafer.gerenciadorcripto.infrastructure.repository.CorretoraRepository
import br.com.nafer.gerenciadorcripto.infrastructure.repository.UsuarioRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Service
class CarteiraService(
    val carteiraRepository: CarteiraRepository,
    val moedaService: MoedaService,
    val ativosRepository: AtivosRepository,
    val usuarioRepository: UsuarioRepository,
    val corretoraRepository: CorretoraRepository,
    val mapper: CarteiraMapper
) {
    fun obterCarteiras(): List<CarteiraResponse> {
        return carteiraRepository.findAll()
            .map { mapper.toResponse(it) }
            .sortedBy { it.nome }
    }

    fun criarCarteira(request: CarteiraRequest): Carteira {
        val idUsuario = request.usuario.idUsuario
        val idCorretora = request.corretora.idCorretora
        val usuario = usuarioRepository.findById(idUsuario).orElseThrow { NotFoundException("Usuario não encontrado: $idUsuario") }
        val corretora = corretoraRepository.findById(idCorretora).orElseThrow {NotFoundException("Corretora: não encontrada: $idCorretora")}
        val carteira = mapper.toEntity(request, usuario, corretora)
        return carteiraRepository.save(carteira)
    }

    fun obterCarteira(usuario: List<Usuario>? = null, corretora: List<Corretora>? = null): List<Carteira>? {
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
}