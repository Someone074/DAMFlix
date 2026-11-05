package com.example.damflix

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.damflix.model.AppDatabase
import com.example.damflix.model.MovieRepository
import com.example.damflix.ui.screens.MovieCatalog
import com.example.damflix.ui.screens.ProfileScreen
import com.example.damflix.ui.screens.MovieForm
import com.example.damflix.viewmodel.MovieViewModel
import com.example.damflix.ui.theme.DAMFlixTheme
import com.example.damflix.viewmodel.MovieViewModelFactory
import com.google.android.material.color.utilities.TonalPalette
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Contenedor global de la aplicación
        val app = application as DAMFlixApplication

        // Fábrica con repositorio Singleton almacenado en Application
        val factory = MovieViewModelFactory(app.repository)

        setContent {
            DAMFlixTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Obtiención/Mantenimiento del ViewModel (retenido ante rotaciones mediante la fábrica)
                        val viewModel: MovieViewModel = viewModel(factory = factory)

                        // 4. Invocamos la pantalla principal pasando el ViewModel
                        AppDAMFlixMainScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(250.dp) // Reducir ancho del menú
            ) {
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
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            color = Color.White,
                            fontSize = 40.sp, // Para cambiar el tamaño de la letra
                            fontWeight = FontWeight.Bold, // Negrita
                            textAlign = TextAlign.Center, // Centrado
                            // El título necesita ocupar el ancho disponible para que se note el centrado
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    expandedHeight = 80.dp // Agranda la TopBar
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
            // Posición del botón (abajo a la izquierda)
            floatingActionButtonPosition = FabPosition.Start
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Ruta.Inicio.path,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Ruta.Inicio.path) {
                    Column {
                        ProfileScreen(viewModel = viewModel)
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
