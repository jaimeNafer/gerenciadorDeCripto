package br.com.nafer.gerenciadorcripto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.nafer.gerenciadorcripto.dtos.binance.ArquivoDTO
import br.com.nafer.gerenciadorcripto.dtos.binance.CorretoraDTO
import br.com.nafer.gerenciadorcripto.dtos.binance.UsuarioDTO
import br.com.nafer.gerenciadorcripto.ui.components.common.WindowInfo
import br.com.nafer.gerenciadorcripto.ui.components.listagemArquivosImportados
import br.com.nafer.gerenciadorcripto.ui.components.modalImportacaoCSV
import br.com.nafer.gerenciadorcripto.ui.viewmodel.ArquivoViewModel
import br.com.nafer.gerenciadorcripto.ui.viewmodel.ImportacaoViewModel
import br.com.nafer.gerenciadorcripto.ui.viewmodel.OperacaoViewModel
import org.springframework.context.ConfigurableApplicationContext

@Composable
fun telaDeImportacaoCsv(applicationContext: ConfigurableApplicationContext, windowInfo: WindowInfo) {
    val arquivoViewModel = applicationContext.getBean(ArquivoViewModel::class.java)
    val operacaoViewModel = applicationContext.getBean(OperacaoViewModel::class.java)
    val importacaoViewModel = applicationContext.getBean(ImportacaoViewModel::class.java)

    val arquivosImportados = remember {
        mutableStateListOf<ArquivoDTO>().apply {
            addAll(arquivoViewModel.listar())
        }
    }

    val exibirDialog = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { exibirDialog.value = true }) {
            Text("Importar Novo Arquivo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        listagemArquivosImportados(
            arquivos = arquivosImportados,
            onExcluirClick = {
                arquivosImportados.remove(it)
                importacaoViewModel.processarExclusaoArquivo(it)
            }
        )
    }

    if (exibirDialog.value) {
        AlertDialog(
            onDismissRequest = { exibirDialog.value = false },
            buttons = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    modalImportacaoCSV(
                        applicationContext = applicationContext,
                        aoImportar = { usuario, corretora, arquivo ->
                            importacaoViewModel.processarImportacaoArquivo(arquivo, corretora.idCorretora!!, usuario.idUsuario!!)
                            arquivosImportados.add(arquivo)
                        }
                    ) { exibirDialog.value = false }
                }
            },
            title = null,
            text = null,
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color.White
        )
    }
}