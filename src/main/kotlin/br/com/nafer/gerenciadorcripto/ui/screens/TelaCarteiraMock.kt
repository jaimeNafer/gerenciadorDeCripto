package br.com.nafer.gerenciadorcripto.ui.screens

import androidx.compose.runtime.Composable
import java.math.BigDecimal

@Composable
fun TelaDeCarteiraMockada() {
    val patrimonio = PatrimonioDTO(
        valorInvestido = BigDecimal("100000.00"),
        valorAtual = BigDecimal("125000.00"),
        lucroPrejuizo = BigDecimal("25000.00"),
        percentualVariacao = BigDecimal("25.00")
    )

    val historico = listOf(
        HistoricoMensalDTO("Jan", BigDecimal("80000")),
        HistoricoMensalDTO("Fev", BigDecimal("95000")),
        HistoricoMensalDTO("Mar", BigDecimal("110000")),
        HistoricoMensalDTO("Abr", BigDecimal("120000")),
        HistoricoMensalDTO("Mai", BigDecimal("125000")),
        HistoricoMensalDTO("Jun", BigDecimal("80000")),
        HistoricoMensalDTO("Jul", BigDecimal("95000")),
        HistoricoMensalDTO("Ago", BigDecimal("110000")),
        HistoricoMensalDTO("Set", BigDecimal("120000")),
        HistoricoMensalDTO("Out", BigDecimal("125000")),
        HistoricoMensalDTO("Nov", BigDecimal("80000")),
        HistoricoMensalDTO("Dez", BigDecimal("95000")),
        HistoricoMensalDTO("Jan", BigDecimal("110000")),
        HistoricoMensalDTO("Fev", BigDecimal("120000")),
        HistoricoMensalDTO("Mar", BigDecimal("125000")),
        HistoricoMensalDTO("Jan", BigDecimal("100000")),
        HistoricoMensalDTO("Fev", BigDecimal("95000")),
        HistoricoMensalDTO("Mar", BigDecimal("110000")),
        HistoricoMensalDTO("Abr", BigDecimal("120000")),
        HistoricoMensalDTO("Mai", BigDecimal("125000")),
        HistoricoMensalDTO("Jun", BigDecimal("80000")),
        HistoricoMensalDTO("Jul", BigDecimal("95000")),
        HistoricoMensalDTO("Ago", BigDecimal("110000")),
        HistoricoMensalDTO("Set", BigDecimal("120000")),
        HistoricoMensalDTO("Out", BigDecimal("125000")),
        HistoricoMensalDTO("Nov", BigDecimal("80000")),
        HistoricoMensalDTO("Dez", BigDecimal("95000")),
        HistoricoMensalDTO("Jan", BigDecimal("110000")),
        HistoricoMensalDTO("Fev", BigDecimal("120000")),
        HistoricoMensalDTO("Mar", BigDecimal("125000")),
    )

    val moedas = listOf(
        AtivoCarteiraDTO("BTC", "Bitcoin", "https://coin-images.coingecko.com/coins/images/1/thumb/bitcoin.png", BigDecimal("0.15"), BigDecimal("42000"), BigDecimal("35000"), BigDecimal("39000"), BigDecimal("7000")),
        AtivoCarteiraDTO("ETH", "Ethereum", "https://coin-images.coingecko.com/coins/images/279/thumb/ethereum.png", BigDecimal("2.0"), BigDecimal("18000"), BigDecimal("20000"), BigDecimal("19000"), BigDecimal("-2000")),
        AtivoCarteiraDTO("SOL", "Solana", "https://coin-images.coingecko.com/coins/images/4128/thumb/solana.png", BigDecimal("50"), BigDecimal("16000"), BigDecimal("15000"), BigDecimal("300"), BigDecimal("1000")),
        AtivoCarteiraDTO("BTC", "Bitcoin", "https://coin-images.coingecko.com/coins/images/1/thumb/bitcoin.png", BigDecimal("0.15"), BigDecimal("42000"), BigDecimal("35000"), BigDecimal("39000"), BigDecimal("7000")),
        AtivoCarteiraDTO("ETH", "Ethereum", "https://coin-images.coingecko.com/coins/images/279/thumb/ethereum.png", BigDecimal("2.0"), BigDecimal("18000"), BigDecimal("20000"), BigDecimal("19000"), BigDecimal("-2000")),
        AtivoCarteiraDTO("SOL", "Solana", "https://coin-images.coingecko.com/coins/images/4128/thumb/solana.png", BigDecimal("50"), BigDecimal("16000"), BigDecimal("15000"), BigDecimal("300"), BigDecimal("1000")),
        AtivoCarteiraDTO("BTC", "Bitcoin", "https://coin-images.coingecko.com/coins/images/1/thumb/bitcoin.png", BigDecimal("0.15"), BigDecimal("42000"), BigDecimal("35000"), BigDecimal("39000"), BigDecimal("7000")),
        AtivoCarteiraDTO("ETH", "Ethereum", "https://coin-images.coingecko.com/coins/images/279/thumb/ethereum.png", BigDecimal("2.0"), BigDecimal("18000"), BigDecimal("20000"), BigDecimal("19000"), BigDecimal("-2000")),
        AtivoCarteiraDTO("SOL", "Solana", "https://coin-images.coingecko.com/coins/images/4128/thumb/solana.png", BigDecimal("50"), BigDecimal("16000"), BigDecimal("15000"), BigDecimal("300"), BigDecimal("1000")),
        AtivoCarteiraDTO("BTC", "Bitcoin", "https://coin-images.coingecko.com/coins/images/1/thumb/bitcoin.png", BigDecimal("0.15"), BigDecimal("42000"), BigDecimal("35000"), BigDecimal("39000"), BigDecimal("7000")),
        AtivoCarteiraDTO("ETH", "Ethereum", "https://coin-images.coingecko.com/coins/images/279/thumb/ethereum.png", BigDecimal("2.0"), BigDecimal("18000"), BigDecimal("20000"), BigDecimal("19000"), BigDecimal("-2000")),
        AtivoCarteiraDTO("BTC", "Bitcoin", "https://coin-images.coingecko.com/coins/images/1/thumb/bitcoin.png", BigDecimal("0.15"), BigDecimal("42000"), BigDecimal("35000"), BigDecimal("39000"), BigDecimal("7000")),
        AtivoCarteiraDTO("ETH", "Ethereum", "https://coin-images.coingecko.com/coins/images/279/thumb/ethereum.png", BigDecimal("2.0"), BigDecimal("18000"), BigDecimal("20000"), BigDecimal("19000"), BigDecimal("-2000")),
        AtivoCarteiraDTO("SOL", "Solana", "https://coin-images.coingecko.com/coins/images/4128/thumb/solana.png", BigDecimal("50"), BigDecimal("16000"), BigDecimal("15000"), BigDecimal("300"), BigDecimal("1000")),
        AtivoCarteiraDTO("BTC", "Bitcoin", "https://coin-images.coingecko.com/coins/images/1/thumb/bitcoin.png", BigDecimal("0.15"), BigDecimal("42000"), BigDecimal("35000"), BigDecimal("39000"), BigDecimal("7000")),
        AtivoCarteiraDTO("ETH", "Ethereum", "https://coin-images.coingecko.com/coins/images/279/thumb/ethereum.png", BigDecimal("2.0"), BigDecimal("18000"), BigDecimal("20000"), BigDecimal("19000"), BigDecimal("-2000")),
        AtivoCarteiraDTO("SOL", "Solana", "https://coin-images.coingecko.com/coins/images/4128/thumb/solana.png", BigDecimal("50"), BigDecimal("16000"), BigDecimal("15000"), BigDecimal("300"), BigDecimal("1000")),
        AtivoCarteiraDTO("BTC", "Bitcoin", "https://coin-images.coingecko.com/coins/images/1/thumb/bitcoin.png", BigDecimal("0.15"), BigDecimal("42000"), BigDecimal("35000"), BigDecimal("39000"), BigDecimal("7000")),
        AtivoCarteiraDTO("ETH", "Ethereum", "https://coin-images.coingecko.com/coins/images/279/thumb/ethereum.png", BigDecimal("2.0"), BigDecimal("18000"), BigDecimal("20000"), BigDecimal("19000"), BigDecimal("-2000")),
        AtivoCarteiraDTO("SOL", "Solana", "https://coin-images.coingecko.com/coins/images/4128/thumb/solana.png", BigDecimal("50"), BigDecimal("16000"), BigDecimal("15000"), BigDecimal("300"), BigDecimal("1000")),
        AtivoCarteiraDTO("BTC", "Bitcoin", "https://coin-images.coingecko.com/coins/images/1/thumb/bitcoin.png", BigDecimal("0.15"), BigDecimal("42000"), BigDecimal("35000"), BigDecimal("39000"), BigDecimal("7000")),
        AtivoCarteiraDTO("ETH", "Ethereum", "https://coin-images.coingecko.com/coins/images/279/thumb/ethereum.png", BigDecimal("2.0"), BigDecimal("18000"), BigDecimal("20000"), BigDecimal("19000"), BigDecimal("-2000")),
        AtivoCarteiraDTO("SOL", "Solana", "https://coin-images.coingecko.com/coins/images/4128/thumb/solana.png", BigDecimal("258"), BigDecimal("16000"), BigDecimal("15000"), BigDecimal("300"), BigDecimal("1000")),
    )

    TelaDeCarteira(
        patrimonio = patrimonio,
        historico = historico,
        moedas = moedas,
        onNovaOperacao = { moeda -> println("Nova operação para ${moeda.ticker}") },
        onVerOperacoes = { moeda -> println("Ver operações de ${moeda.ticker}") }
    )
}
