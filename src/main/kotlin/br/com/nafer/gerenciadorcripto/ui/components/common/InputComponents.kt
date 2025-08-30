package br.com.nafer.gerenciadorcripto.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Campo de texto padrão com rótulo e validação
 * @param value Valor atual do campo
 * @param onValueChange Callback para alteração do valor
 * @param label Rótulo do campo
 * @param modifier Modificador do campo
 * @param isError Indica se o campo contém erro
 * @param errorMessage Mensagem de erro (opcional)
 * @param leadingIcon Ícone à esquerda (opcional)
 * @param trailingIcon Ícone à direita (opcional)
 * @param keyboardOptions Opções de teclado
 * @param singleLine Define se o campo é de linha única
 * @param maxLines Número máximo de linhas
 */
@Composable
fun StandardTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    leadingIcon: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    visualTransformation: VisualTransformation? = null,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = isError,
            leadingIcon = leadingIcon,
            trailingIcon = if (isError) {
                {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Erro",
                        tint = MaterialTheme.colors.error
                    )
                }
            } else trailingIcon,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth()
        )
        
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

/**
 * Campo de texto para senha com botão de visibilidade
 * @param value Valor atual do campo
 * @param onValueChange Callback para alteração do valor
 * @param label Rótulo do campo
 * @param modifier Modificador do campo
 * @param isError Indica se o campo contém erro
 * @param errorMessage Mensagem de erro (opcional)
 */
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    
    StandardTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        isError = isError,
        errorMessage = errorMessage,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha"
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

/**
 * Campo de texto numérico
 * @param value Valor atual do campo
 * @param onValueChange Callback para alteração do valor
 * @param label Rótulo do campo
 * @param modifier Modificador do campo
 * @param isError Indica se o campo contém erro
 * @param errorMessage Mensagem de erro (opcional)
 */
@Composable
fun NumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    StandardTextField(
        value = value,
        onValueChange = { newValue ->
            // Aceita apenas números e ponto decimal
            if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                onValueChange(newValue)
            }
        },
        label = label,
        modifier = modifier,
        isError = isError,
        errorMessage = errorMessage,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}

/**
 * Campo de seleção com rótulo
 * @param label Rótulo do campo
 * @param options Lista de opções
 * @param selectedOption Opção selecionada
 * @param onOptionSelected Callback para seleção de opção
 * @param modifier Modificador do campo
 */
@Composable
fun <T> SelectField(
    label: String,
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    optionToString: (T) -> String = { it.toString() },
    leadingIcon: ImageVector? = null
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                
                Text(
                    text = selectedOption?.let { optionToString(it) } ?: "Selecione",
                    modifier = Modifier.weight(1f)
                )
                
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }) {
                            Text(optionToString(option))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Campo de data com ícone de calendário
 * @param value Valor atual do campo
 * @param onValueChange Callback para alteração do valor
 * @param label Rótulo do campo
 * @param modifier Modificador do campo
 * @param isError Indica se o campo contém erro
 * @param errorMessage Mensagem de erro (opcional)
 * @param onDatePickerClick Callback para clique no ícone de calendário
 */
@Composable
fun DateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    onDatePickerClick: () -> Unit
) {
    StandardTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        isError = isError,
        errorMessage = errorMessage,
        trailingIcon = {
            IconButton(onClick = onDatePickerClick) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Selecionar data"
                )
            }
        },
    )
}