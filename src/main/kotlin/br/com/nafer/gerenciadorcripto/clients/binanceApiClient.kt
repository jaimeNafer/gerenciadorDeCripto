package br.com.nafer.gerenciadorcripto.clients

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset


@Component
class BinanceApiClient(@Value("\${api.url.binance}") private val baseUrl: String) {

    private val restTemplate = RestTemplate()

    data class PrecoAtivo(val precoBrl: BigDecimal)

    data class BinanceDto(
        val horaAbertura: Long? = null,
        val precoAbertura: String? = null,
        val maxima: String? = null,
        val minima: String? = null,
        val precoFechamento: BigDecimal,
        val volume: String? = null,
        val horaFechamento: Long? = null
    )

    fun buscarPrecoDaMoedaPorTicker(ticker: String, data: LocalDateTime): PrecoAtivo {
        val dataAbertura = data.withMinute(0).withSecond(0).withNano(0).toInstant(ZoneOffset.UTC)?.toEpochMilli()
        val dataFechamento = data.plusMinutes(1)?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
        val urlObterValorUsdTBrl = baseUrl.plus("/api/v3/klines?symbol=USDTBRL&interval=1h&startTime=$dataAbertura&endTime=$dataFechamento")
        val typeRef = object : ParameterizedTypeReference<List<List<String>>>() {}
        val valorHistoricoBrl = BigDecimal(restTemplate.exchange(urlObterValorUsdTBrl, HttpMethod.GET, null, typeRef).body?.get(0)?.get(4)!!)

        val stableCoins = listOf("BRL", "USDC", "USDT")
        var indice = 0
        return executarComRetry(3) {
            val stableCoin = stableCoins[indice++]
            val url = baseUrl.plus("/api/v3/klines?symbol=${ticker.uppercase().plus(stableCoin)}&interval=1h&startTime=$dataAbertura&endTime=$dataFechamento")
            val response = restTemplate.exchange(url, HttpMethod.GET, null, typeRef)
            val body = response.getBody()?.get(0)!!
            val valorEmBrl =  if (stableCoin == "BRL") BigDecimal(body[4]) else BigDecimal(body[4]).multiply(valorHistoricoBrl)
            val binanceDto = BinanceDto(precoFechamento = valorEmBrl)
            PrecoAtivo(binanceDto.precoFechamento)
        }
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
