package br.com.nafer.gerenciadorcripto.ui.screens

import Formatador
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import br.com.nafer.gerenciadorcripto.ui.components.ResumoCarteira
import br.com.nafer.gerenciadorcripto.ui.components.graficoEvolucaoPatrimonioChart
import br.com.nafer.gerenciadorcripto.ui.components.listagemAtivos
import br.com.nafer.gerenciadorcripto.ui.viewmodel.OperacaoViewModel
import org.springframework.context.ConfigurableApplicationContext
import java.math.BigDecimal
import java.net.URL

// DTOs auxiliares usados pela tela

data class PatrimonioDTO(
    val valorInvestido: BigDecimal,
    val valorAtual: BigDecimal,
    val lucroPrejuizo: BigDecimal,
    val percentualVariacao: BigDecimal
)

data class HistoricoMensalDTO(
    val mes: String,
    val valorTotal: BigDecimal
)

data class AtivoCarteiraDTO(
    val ticker: String,
    val nome: String,
    val icone: String,
    val quantidade: BigDecimal,
    val valorAtual: BigDecimal,
    val valorInvestido: BigDecimal,
    val precoMedio: BigDecimal,
    val lucroPrejuizo: BigDecimal
)

@Composable
fun IconMoeda(url: String) {
    val cache = remember { mutableMapOf<String, ImageBitmap?>() }
    var image by remember(url) { mutableStateOf(cache[url]) }

    LaunchedEffect(url) {
        if (url.isNotBlank() && cache[url] == null) {
            runCatching {
                val bytes = URL(url).readBytes()
                org.jetbrains.skia.Image.makeFromEncoded(bytes).toComposeImageBitmap()
            }.onSuccess {
                cache[url] = it
                image = it
            }
        }
    }

    Box(modifier = Modifier.size(32.dp)) {
        image?.let {
            Image(
                painter = BitmapPainter(it),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}


@Composable
fun IconButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    androidx.compose.material.IconButton(onClick = onClick) {
        content()
    }
}

@Composable
fun TelaDeCarteira(
    patrimonio: PatrimonioDTO,
    historico: List<HistoricoMensalDTO>,
    moedas: List<AtivoCarteiraDTO>,
    onNovaOperacao: (moeda: AtivoCarteiraDTO) -> Unit,
    onVerOperacoes: (moeda: AtivoCarteiraDTO) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            ResumoCarteira(
                valorAtual = patrimonio.valorAtual,
                valorInvestido = patrimonio.valorInvestido,
                lucroPrejuizo = patrimonio.lucroPrejuizo,
                rentabilidadePercentual = patrimonio.percentualVariacao
            )
            Spacer(Modifier.height(16.dp))
            Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth().height(220.dp)) {
                graficoEvolucaoPatrimonioChart(historico)
            }
        }
        item {
            Spacer(Modifier.height(16.dp))
            Text("Meus Ativos", style = MaterialTheme.typography.h6, modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))
            listagemAtivos(
                ativos = moedas,
                onNovaOperacao = onNovaOperacao,
                onEditarOperacao = onVerOperacoes
            )
        }
    }
}

@Composable
fun ColunaResumo(label: String, valor: BigDecimal, percentual: BigDecimal? = null, color: Color = Color.Black) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.caption)
        Text(Formatador.formatarMoeda(valor), style = MaterialTheme.typography.h6, color = color)
        percentual?.let {
            Text("${it.setScale(2)}%", color = if (it >= BigDecimal.ZERO) Color(0xFF2E7D32) else Color.Red)
        }
    }
}

@Composable
fun TelaDeCarteiraPreview(applicationContext: ConfigurableApplicationContext) {
    val  viewModel = applicationContext.getBean(OperacaoViewModel::class.java)
    val operacoes = viewModel.listarOperacoes()
    val patrimonio = PatrimonioDTO(
        valorInvestido = BigDecimal("230000.00"),
        valorAtual = BigDecimal("243000.00"),
        lucroPrejuizo = BigDecimal("13000.00"),
        percentualVariacao = BigDecimal("5.65")
    )

    val historico = listOf(
        HistoricoMensalDTO("01/24", BigDecimal("100000")),
        HistoricoMensalDTO("02/24", BigDecimal("120000")),
        HistoricoMensalDTO("03/24", BigDecimal("150000")),
        HistoricoMensalDTO("04/24", BigDecimal("170000")),
        HistoricoMensalDTO("05/24", BigDecimal("243000"))
    )

    val ativos = listOf(
        AtivoCarteiraDTO("BTC", "Bitcoin", "btc.png", BigDecimal("0.15"), BigDecimal("160000"), BigDecimal("120000"), BigDecimal("800000"), BigDecimal("40000")),
        AtivoCarteiraDTO("ETH", "Ethereum", "eth.png", BigDecimal("3.3"), BigDecimal("42000"), BigDecimal("52000"), BigDecimal("16000"), BigDecimal("-10000"))
    )

    TelaDeCarteira(
        patrimonio = patrimonio,
        historico = historico,
        moedas = ativos,
        onNovaOperacao = {},
        onVerOperacoes = {}
    )
}

fun colorFromValue(valor: BigDecimal): Color {
    return when {
        valor > BigDecimal.ZERO -> Color(0xFF2E7D32) // verde
        valor < BigDecimal.ZERO -> Color.Red
        else -> Color.Gray
    }
}
