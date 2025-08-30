package br.com.nafer.gerenciadorcripto.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import br.com.nafer.gerenciadorcripto.dtos.binance.ArquivoDTO
import br.com.nafer.gerenciadorcripto.dtos.binance.CorretoraDTO
import br.com.nafer.gerenciadorcripto.dtos.binance.UsuarioDTO
import br.com.nafer.gerenciadorcripto.ui.viewmodel.ArquivoViewModel
import br.com.nafer.gerenciadorcripto.ui.viewmodel.CorretoraViewModel
import br.com.nafer.gerenciadorcripto.ui.viewmodel.OperacaoViewModel
import br.com.nafer.gerenciadorcripto.ui.viewmodel.UsuarioViewModel
import org.springframework.context.ConfigurableApplicationContext
import java.awt.FileDialog
import java.awt.Frame
import java.io.File


@Composable
fun modalImportacaoCSV(
    applicationContext: ConfigurableApplicationContext,
    aoImportar: (UsuarioDTO, CorretoraDTO, ArquivoDTO) -> Unit,
    aoCancelar: () -> Unit
) {
    var mensagem by remember { mutableStateOf<String?>(null) }
    val usuarioViewModel = applicationContext.getBean(UsuarioViewModel::class.java)
    val corretoraViewModel = applicationContext.getBean(CorretoraViewModel::class.java)

    val usuarios = usuarioViewModel.listarTodosUsuarios()
    val corretoras = corretoraViewModel.listarTodasCorretoras()

    var arquivoSelecionado by remember { mutableStateOf<ArquivoDTO?>(null) }
    var usuarioSelecionado by remember { mutableStateOf(usuarios.first()) }
    var corretoraSelecionada by remember { mutableStateOf(corretoras.first()) }

    Card(
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(min = 480.dp, max = 600.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Importação de Arquivo CSV", style = MaterialTheme.typography.h6)

            DropdownSelector("Usuário", usuarios, usuarioSelecionado, { usuarioSelecionado = it }, { it.nome })
            DropdownSelector(
                "Corretora",
                corretoras,
                corretoraSelecionada,
                { corretoraSelecionada = it },
                { it.nome })

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Caminho do arquivo",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )

                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    OutlinedTextField(
                        value = arquivoSelecionado?.caminho ?: "",
                        onValueChange = { arquivoSelecionado?.caminho = it },
                        label = { Text("Caminho do arquivo CSV") },
                        modifier = Modifier.fillMaxWidth().padding(end = 48.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                    )
                    IconButton(
                        onClick = {
                            val fileDialog = FileDialog(null as Frame?, "Selecionar CSV", FileDialog.LOAD)
                            fileDialog.isVisible = true
                            val file = fileDialog.file?.let { File(fileDialog.directory, it) }
                            if (file != null) {
                                arquivoSelecionado = ArquivoDTO(
                                    idArquivo = null,
                                    caminho = file.absolutePath,
                                    nome = file.name,
                                    conteudo = file.readBytes(),
                                    separador = ';'
                                )
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(Icons.Default.FolderOpen, contentDescription = "Selecionar arquivo CSV")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = aoCancelar) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            if (arquivoSelecionado?.caminho?.isNotBlank() == true) {
                                try {
                                    mensagem = "✅ Importação realizada com sucesso!"
                                    aoImportar(usuarioSelecionado, corretoraSelecionada, arquivoSelecionado!!,)
                                } catch (e: Exception) {
                                    mensagem = "❌ Erro ao importar: ${e.message}"
                                }
                            } else {
                                mensagem = "⚠️ Informe o caminho do arquivo."
                            }
                        }
                    ) {
                        Text("Importar")
                    }
                }

                mensagem?.let {
                    Text(
                        it,
                        color = when {
                            it.startsWith("✅") -> Color(0xFF2E7D32)
                            it.startsWith("❌") -> Color(0xFFC62828)
                            else -> Color(0xFFEF6C00)
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}