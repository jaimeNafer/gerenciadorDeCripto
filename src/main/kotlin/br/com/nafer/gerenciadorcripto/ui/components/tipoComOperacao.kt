package br.com.nafer.gerenciadorcripto.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun TipoComOperacao(
    tipo: String?,
    operacao: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        // Se tipo não for nulo/vazio e não for "-", exibe tipo em destaque e operação abaixo
        if (!tipo.isNullOrEmpty() && tipo != "-") {
            Text(tipo, style = MaterialTheme.typography.body2)
            if (!operacao.isNullOrEmpty() && operacao != tipo) {
                Text(operacao, fontSize = 11.sp, color = Color.Gray)
            }
        }
        // Se tipo for nulo/vazio ou "-", exibe operação em destaque
        else if (!operacao.isNullOrEmpty()) {
            Text(operacao, style = MaterialTheme.typography.body2)
        }
        // Se ambos forem nulos/vazios, exibe "-"
        else {
            Text("-", style = MaterialTheme.typography.body2)
        }
    }
}