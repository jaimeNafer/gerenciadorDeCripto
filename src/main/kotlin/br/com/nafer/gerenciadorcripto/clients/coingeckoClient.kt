package br.com.nafer.gerenciadorcripto.clients

import br.com.nafer.gerenciadorcripto.domain.model.Moeda
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class CoingeckoClient(@Value("\${api.url.coingecko}") private val baseUrl: String) {

    private val restTemplate = RestTemplate()
    private val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private val mapper = jacksonObjectMapper()

    data class PrecoAtivo(
        val precoBrl: BigDecimal,
        val precoUsd: BigDecimal
    )

    fun buscarPrecoDaMoedaPorNome(nome: String, data: LocalDate): PrecoAtivo {
        val dataFormatada = data.format(dateFormatter)
        val url = baseUrl.plus("/api/v3/coins/$nome/history?date=$dataFormatada&localization=false")

        return executarComRetry(3) {
            val response = restTemplate.getForObject(url, Map::class.java)
                ?: throw RuntimeException("Resposta nula da API CoinGecko para $nome")

            val marketData = response["market_data"] as? Map<*, *>
                ?: throw RuntimeException("market_data não encontrado")

            val currentPrice = marketData["current_price"] as? Map<*, *>
                ?: throw RuntimeException("current_price não encontrado")

            val precoUsd = currentPrice["usd"]?.toString()?.toBigDecimalOrNull()
                ?: throw RuntimeException("Preço USD ausente para $nome")

            val precoBrl = currentPrice["brl"]?.toString()?.toBigDecimalOrNull()
                ?: buscarCambioUsdBrl(data).multiply(precoUsd)

            PrecoAtivo(precoBrl, precoUsd)
        }
    }

    fun buscarMoedasOrdenadaPorMarketCap(ticker: String): Moeda {
        val response = executarComRetry(3) {
            val url = baseUrl.plus("/api/v3/search?query=$ticker")
            restTemplate.getForEntity(url, String::class.java).body
                ?: throw RuntimeException("Não foi possível obter a moedas $ticker")
        }

        val rootNode = mapper.readTree(response)
        val coinNode = rootNode["coins"]
            ?.firstOrNull { coin->
                coin["api_symbol"].asText()?.uppercase() == ticker.uppercase() || coin["symbol"].asText()?.uppercase() == ticker.uppercase() }

        return coinNode?.let { coin-> Moeda(ticker = coin["symbol"].asText(), nome = coin["name"].asText(), idCoingecko = coin["id"].asText(), icone = coin["thumb"].asText()) } ?: Moeda(ticker = ticker)
    }

    private fun buscarCambioUsdBrl(data: LocalDate): BigDecimal {
        val dataFormatada = data.format(dateFormatter)
        val url = baseUrl.plus("/api/v3/coins/usd/history?date=$dataFormatada&localization=false")

        val response = restTemplate.getForObject(url, Map::class.java)
            ?: throw RuntimeException("Erro ao buscar cotação USD/BRL")

        val marketData = response["market_data"] as? Map<*, *> ?: throw RuntimeException("market_data ausente")
        val currentPrice = marketData["current_price"] as? Map<*, *> ?: throw RuntimeException("current_price ausente")
        return currentPrice["brl"]?.toString()?.toBigDecimalOrNull()
            ?: throw RuntimeException("Cotação BRL não encontrada")
    }

    private fun <T> executarComRetry(tentativas: Int, bloco: () -> T): T {
        var ultimaExcecao: Exception? = null
        repeat(tentativas) {
            try {
                return bloco()
            } catch (e: Exception) {
                ultimaExcecao = e
                Thread.sleep(1000)
            }
        }
        throw RuntimeException("Erro após $tentativas tentativas: ${ultimaExcecao?.message}", ultimaExcecao)
    }
}
