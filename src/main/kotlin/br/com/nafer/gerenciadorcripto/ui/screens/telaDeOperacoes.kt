package br.com.nafer.gerenciadorcripto.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.nafer.gerenciadorcripto.ui.components.listagemDeOperacoes
import br.com.nafer.gerenciadorcripto.ui.viewmodel.OperacaoViewModel
import org.springframework.context.ConfigurableApplicationContext

@Composable
fun telaDeOperacoes(applicationContext: ConfigurableApplicationContext) {
    val  viewModel = applicationContext.getBean(OperacaoViewModel::class.java)
    val operacoes = viewModel.listarOperacoes()
    Column(modifier = Modifier.fillMaxSize()) {
        listagemDeOperacoes(operacoes)
    }
}