package br.com.nafer.gerenciadorcripto.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.nafer.gerenciadorcripto.navigation.Screen

@Composable
fun AppDrawer(
    currentScreen: Screen,
    onDestinationClicked: (Screen) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Menu", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))
        Screen.values().forEach { screen ->
            Row(
                modifier = Modifier.fillMaxWidth().clickable { onDestinationClicked(screen) }.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(screen.icone, contentDescription = screen.title)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = screen.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDestinationClicked(screen) }
                        .padding(8.dp),
                    color = if (screen == currentScreen) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                )
            }
        }
    }
}
