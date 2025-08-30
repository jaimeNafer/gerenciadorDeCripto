package br.com.nafer.gerenciadorcripto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.nafer.gerenciadorcripto.dtos.binance.ArquivoDTO
import br.com.nafer.gerenciadorcripto.ui.viewmodel.OperacaoViewModel

@Composable
fun listagemArquivosImportados(
    arquivos: List<ArquivoDTO>,
    onExcluirClick: (ArquivoDTO) -> Unit
) {
    var arquivoParaExcluir by remember { mutableStateOf<ArquivoDTO?>(null)}
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "📂 Arquivos Importados",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (arquivos.isEmpty()) {
            Text(
                "Nenhum arquivo importado ainda.",
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(8.dp)
            )
        } else {
            arquivos.forEach { arquivo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .background(Color(0xFFF9F9F9))
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "📄 ${arquivo.nome}", style = MaterialTheme.typography.subtitle1)
                            Text(
                                text = "📁 ${arquivo.caminho}",
                                style = MaterialTheme.typography.caption,
                                color = Color.Gray
                            )
                        }

                        IconButton(
                            onClick = { arquivoParaExcluir = arquivo },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }

    if (arquivoParaExcluir != null) {
        AlertDialog(
            onDismissRequest = { arquivoParaExcluir = null },
            title = { Text("Confirmar Exclusão") },
            text = {
                Text("Tem certeza que deseja excluir o arquivo \"${arquivoParaExcluir?.nome}\"?")
            },
            confirmButton = {
                TextButton(onClick = {
                    onExcluirClick(arquivoParaExcluir!!)
                    arquivoParaExcluir = null
                }) {
                    Text("Excluir", color = Color.Red)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { arquivoParaExcluir = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}