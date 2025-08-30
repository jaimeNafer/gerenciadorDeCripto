package br.com.nafer.gerenciadorcripto.service

import br.com.nafer.gerenciadorcripto.domain.model.Ativos
import br.com.nafer.gerenciadorcripto.domain.model.Carteira
import br.com.nafer.gerenciadorcripto.domain.model.Corretora
import br.com.nafer.gerenciadorcripto.infrastructure.repository.CarteiraRepository
import br.com.nafer.gerenciadorcripto.domain.model.Usuario
import br.com.nafer.gerenciadorcripto.infrastructure.repository.AtivosRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class CarteiraService(
    val carteiraRepository: CarteiraRepository,
    val moedaService: MoedaService,
    val ativosRepository: AtivosRepository,
) {
    fun obterCarteira(usuario: List<Usuario>? = null, corretora: List<Corretora>? = null): List<Carteira>? {
        return when {
            usuario?.isNotEmpty() == true && corretora?.isNotEmpty() == true -> carteiraRepository.filtrarCarteiraPorUsuariosECorretoras(
                usuario,
                corretora
            )

            usuario?.isNotEmpty() == true -> carteiraRepository.filtrarAtivosPorUsuarios(usuario)
            corretora?.isNotEmpty() == true -> carteiraRepository.filtrarAtivosPorCorretoras(corretora)
            else -> emptyList()
        }
    }

    fun atualizarAtivo(
        usuario: Usuario,
        corretora: Corretora,
        ticker: String,
        quantidadeTotal: BigDecimal,
        valorTotal: BigDecimal,
        precoMedioTotal: BigDecimal,
        lucroPrejuizoTotal: BigDecimal,
    ) {
        val moeda = moedaService.obterMoedaPorTicker(ticker)
        val carteira = carteiraRepository.filtrarCarteiraPorUsuarioECorretora(usuario, corretora) ?: run {
            criarCarteira(
                usuario,
                corretora
            )
        }

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
        return carteiraRepository.save(Carteira(usuario = usuario, corretora = corretora))
    }

    private fun criarAtivo(carteira: Carteira) {

    }
}