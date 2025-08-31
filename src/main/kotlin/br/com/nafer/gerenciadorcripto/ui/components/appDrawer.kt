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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDestinationClicked(screen) }
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = screen.icone, 
                    contentDescription = screen.title,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = screen.title,
                    color = if (screen == currentScreen) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.caption,
                    maxLines = 2,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}
