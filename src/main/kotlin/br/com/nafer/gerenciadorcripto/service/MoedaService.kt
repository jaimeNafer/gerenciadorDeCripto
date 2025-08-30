package br.com.nafer.gerenciadorcripto.service

import br.com.nafer.gerenciadorcripto.clients.CoingeckoClient
import br.com.nafer.gerenciadorcripto.domain.model.Moeda
import br.com.nafer.gerenciadorcripto.domain.model.dtos.MoedaDTO
import br.com.nafer.gerenciadorcripto.infrastructure.repository.MoedaRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class MoedaService(
    private val moedaRepository: MoedaRepository,
    private val coingeckoClient: CoingeckoClient
) {
    @Cacheable(cacheNames = ["obterMoedaPorTicker"], key = "#ticker.toUpperCase()")
    fun obterMoedaPorTicker(ticker: String): Moeda {
        return moedaRepository.findByIdOrNull(ticker) ?: registrarNovaMoeda(ticker)
    }

    private fun registrarNovaMoeda(ticker: String): Moeda {
        val moeda = try {
            coingeckoClient.buscarMoedasOrdenadaPorMarketCap(ticker)
        } catch (ex: Exception) {
            Moeda(ticker = ticker, nome = ticker)
        }
        return moedaRepository.save(moeda)
    }

    @CacheEvict(cacheNames = ["obterMoedaPorTicker"], allEntries = true)
    fun limparCacheCompleto() {}
}
