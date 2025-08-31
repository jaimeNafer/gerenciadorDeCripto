package br.com.nafer.gerenciadorcripto.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import br.com.nafer.gerenciadorcripto.navigation.Navigation
import br.com.nafer.gerenciadorcripto.navigation.NavigationManager
import br.com.nafer.gerenciadorcripto.navigation.Route
import br.com.nafer.gerenciadorcripto.navigation.getIcon
import br.com.nafer.gerenciadorcripto.navigation.getTitle
import br.com.nafer.gerenciadorcripto.ui.components.common.isCompact
import br.com.nafer.gerenciadorcripto.ui.components.common.rememberWindowInfo
import kotlinx.coroutines.launch
import org.springframework.context.ConfigurableApplicationContext

/**
 * Componente principal da aplicação
 * @param windowState Estado da janela
 * @param applicationContext Contexto da aplicação Spring
 * @param navigationManager Gerenciador de navegação
 */
@Composable
fun MainApp(
    windowState: WindowState,
    applicationContext: ConfigurableApplicationContext,
    navigationManager: NavigationManager = remember { NavigationManager() }
) {
    val windowInfo = rememberWindowInfo(windowState)
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val currentRoute by navigationManager.currentRoute.collectAsState()
    
    val navItems = listOf(
        Route.Home,
        Route.Operacoes,
        Route.Relatorios,
        Route.ImportacaoCSV
    )
    
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(currentRoute.getTitle()) },
                navigationIcon = if (windowInfo.isCompact) {
                    {
                        IconButton(onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                } else null
            )
        },
        drawerContent = if (windowInfo.isCompact) {
            {
                NavigationDrawerContent(
                    navItems = navItems,
                    currentRoute = currentRoute,
                    onNavItemClick = { route ->
                        navigationManager.navigateTo(route)
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                )
            }
        } else null,
        content = { paddingValues ->
            Row(modifier = Modifier.fillMaxSize()) {
                // Barra de navegação lateral para telas médias e grandes
                if (!windowInfo.isCompact) {
                    NavigationRail(
                        navItems = navItems,
                        currentRoute = currentRoute,
                        onNavItemClick = { route ->
                            navigationManager.navigateTo(route)
                        }
                    )
                }
                
                // Conteúdo principal
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Navigation(
                        navigationManager = navigationManager,
                        applicationContext = applicationContext,
                        windowInfo = windowInfo
                    )
                }
            }
        }
    )
}

/**
 * Conteúdo da gaveta de navegação para telas pequenas
 */
@Composable
private fun NavigationDrawerContent(
    navItems: List<Route>,
    currentRoute: Route,
    onNavItemClick: (Route) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp) // Aumenta a largura do drawer
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Gerenciador de Cripto",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(16.dp)
        )
        Divider()
        
        navItems.forEach { route ->
            val isSelected = currentRoute == route
            
            NavigationItem(
                route = route,
                isSelected = isSelected,
                onClick = { onNavItemClick(route) }
            )
        }
    }
}

/**
 * Barra de navegação lateral para telas médias e grandes
 */
@Composable
private fun NavigationRail(
    navItems: List<Route>,
    currentRoute: Route,
    onNavItemClick: (Route) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxHeight(),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.width(100.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            navItems.forEach { route ->
                val isSelected = currentRoute == route
                
                NavigationRailItem(
                    selected = isSelected,
                    onClick = { onNavItemClick(route) },
                    icon = {
                        route.getIcon()?.let { icon ->
                            Icon(icon, contentDescription = route.getTitle())
                        }
                    },
                    label = { Text(route.getTitle()) }
                )
            }
        }
    }
}

/**
 * Item de navegação para a gaveta de navegação
 */
@Composable
private fun NavigationItem(
    route: Route,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colors.primary.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colors.surface
    }
    
    val contentColor = if (isSelected) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.onSurface
    }
    
    Surface(
        color = backgroundColor,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(onClick = onClick),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            val icon = route.getIcon()
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = route.getTitle(),
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = route.getTitle(),
                color = contentColor,
                style = MaterialTheme.typography.caption,
                maxLines = 2,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}