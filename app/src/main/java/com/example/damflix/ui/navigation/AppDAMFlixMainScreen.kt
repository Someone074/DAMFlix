package com.example.damflix.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.damflix.R
import com.example.damflix.ui.screens.MovieCatalog
import com.example.damflix.ui.screens.MovieForm
import com.example.damflix.ui.screens.StartScreen
import com.example.damflix.viewmodel.MovieViewModel
import kotlinx.coroutines.launch

sealed class Ruta(val path: String, val label: String, val icon: ImageVector) {
    object Inicio: Ruta(path = "inicio", label = "Inicio", icon = Icons.Filled.Home)
    object Listado: Ruta(path = "listado", label = "Listado", icon = Icons.Filled.List)
    object Formulario: Ruta(path = "formulario", label = "Añadir", icon = Icons.Filled.Add)
}

@SuppressLint("ViewModelConstructorInComposable")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDAMFlixMainScreen(viewModel: MovieViewModel) {
    val navController = rememberNavController()

    // Estado del Drawer (Abierto / Cerrado)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // CoroutineScope necesario para animar la apertura/cierre
    val coroutineScope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route
    val opcionesMenu = listOf(Ruta.Inicio, Ruta.Listado, Ruta.Formulario)

    val peliculas by viewModel.listaPeliculas.collectAsStateWithLifecycle()
    val nVistas = remember(peliculas) { peliculas.count { it.esVista } }
    val nFavoritas = remember(peliculas) { peliculas.count { it.esFavorita } }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(250.dp) // Reducir ancho del menú
            ) {
                // Cabecera del Menú Lateral (Perfil de Usuario)
                Box(modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 24.dp, top = 0.dp))
                {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.user_avatar),
                            contentDescription = stringResource(R.string.avatar_desc),
                            modifier = Modifier
                                .size(115.dp)
                                .clip(CircleShape) // Avatar circular
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = stringResource(id = R.string.user_name_label),
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        Text(
                            text = stringResource(id = R.string.user_rol),
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        Text(
                            text = stringResource(id = R.string.user_stadistics, nVistas, nFavoritas),
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
                HorizontalDivider()
                Spacer(modifier = Modifier.height(20.dp))

                // Opciones editables del menú
                opcionesMenu.forEach { opcion ->
                    NavigationDrawerItem(
                        icon = { Icon(opcion.icon, contentDescription = opcion.label) },
                        label = { Text(opcion.label) },
                        selected = rutaActual == opcion.path,
                        onClick = {
                            // Cierre asíncrono del panel
                            coroutineScope.launch { drawerState.close() }
                            if (rutaActual != opcion.path) {
                                navController.navigate(opcion.path) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            // Título de la aplicación
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            color = Color.White,
                            fontSize = 40.sp, // Para cambiar el tamaño de la letra
                            fontWeight = FontWeight.Bold, // Negrita
                            textAlign = TextAlign.Center, // Centrado
                            // Ocupar el ancho disponible para destacar el centrado
                            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                        )
                    },
                    /*
                        Puse el botón como un FloatingActionButton por estética
                        Igualmente, dejé el código del botón como lo pedían los requisitos aquí:

                        navigationIcon = {
                            // Botón de hamburguesa para abrir el Drawer
                            IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Abrir Menú")
                            }
                        },
                     */
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            },
            // Botón de hamburguesa para abrir el Drawer
            floatingActionButton = {
                FilledTonalIconButton(
                    onClick = { coroutineScope.launch { drawerState.open() } },
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(56.dp)
                ) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = stringResource(id = R.string.btn_menu)
                    )
                }
            },
            // Posición del botón del Drawer (abajo a la izquierda)
            floatingActionButtonPosition = FabPosition.Start
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Ruta.Inicio.path,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Ruta.Inicio.path) {
                    Column {
                        StartScreen()
                    }
                }
                composable(Ruta.Listado.path) {
                    Column {
                        MovieCatalog(viewModel = viewModel)
                    }
                }
                composable(Ruta.Formulario.path) {
                    Column {
                        MovieForm(
                            viewModel = viewModel,
                            onPeliculaGuardada = {
                                navController.navigate(Ruta.Listado.path) {
                                    popUpTo(Ruta.Inicio.path) { saveState = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
