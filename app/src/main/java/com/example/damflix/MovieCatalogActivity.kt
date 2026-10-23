package com.example.damflix

import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.damflix.ui.theme.DAMFlixTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class MovieCatalogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DAMFlixTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        PantallaCatalogoDAMFlix()
                    }
                }
            }
        }
    }
}

sealed class Ruta(val path: String, val label: String, val icon: ImageVector) {
    object Inicio: Ruta(
        path = "inicio",
        label = "Inicio",
        icon = Icons.Filled.Home
    )
    object Listado: Ruta(
        path = "listado",
        label = "Listado",
        icon = Icons.Filled.List
    )
    object Formulario: Ruta(
        path = "alta",
        label = "Añadir",
        icon = Icons.Filled.Add
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogoDAMFlix() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Column {
                    Text(
                        text = stringResource(id = R.string.name_catalog1),
                        color = Color.White,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = stringResource(id = R.string.name_catalog2),
                        color = Color.White,
                        fontSize = 40.sp, // Para cambiar el tamaño de la letra
                        fontWeight = FontWeight.Bold, // Negrita
                        textAlign = TextAlign.Center, // Centrado
                        // El título necesita ocupar el ancho disponible para que se note el centrado
                        modifier = Modifier.fillMaxWidth()
                    )
                } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                expandedHeight = 110.dp // Agranda la TopBar para que quepa el doble título
            )
        },
        bottomBar = {
            // Observamos la ruta actual del BackStack
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val rutaActual = navBackStackEntry?.destination?.route

            NavigationBar {
                // Definimos una lista inmutable con las rutas
                listOf(
                    Ruta.Inicio,
                    Ruta.Listado,
                    Ruta.Formulario
                ).forEach { ruta ->
                    NavigationBarItem(
                        selected = rutaActual == ruta.path,
                        onClick = {
                            if (rutaActual != ruta.path) {
                                navController.navigate(ruta.path) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(ruta.icon, contentDescription = ruta.label) },
                        label = { Text(ruta.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Ruta.Inicio.path,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Ruta.Inicio.path) {
                MainActivity()
            }
            composable(Ruta.Listado.path) {
                val peliculas = remember {
                    mutableStateListOf(
                        Pelicula(id = 1, titulo = R.string.catalog_movie1_name, detalles = R.string.catalog_movie1_details, esFavorita = true),
                        Pelicula(id = 2, titulo = R.string.catalog_movie2_name, detalles = R.string.catalog_movie2_details, esFavorita = false),
                        Pelicula(id = 3, titulo = R.string.catalog_movie3_name, detalles = R.string.catalog_movie3_details, esFavorita = true),
                        Pelicula(id = 4, titulo = R.string.catalog_movie4_name, detalles = R.string.catalog_movie4_details, esFavorita = false)
                    )
                }

                ListaPeliculas(
                    peliculas = peliculas,
                    onToggleFavorito = { idSeleccionado -> // ID de la película favorita seleccionada
                        val indice = peliculas.indexOfFirst { it.id == idSeleccionado } //
                        if (indice != -1) {
                            val peliculaActual = peliculas[indice] // Guardado de la película de ese ID
                            // Si la película antes no era favorita se convierte en favorita (y viceversa)
                            peliculas[indice] = peliculaActual.copy(esFavorita = !peliculaActual.esFavorita)
                        }
                    }
                )
            }
            composable(Ruta.Formulario.path) {
                MovieFormActivity()
            }
        }
    }
}

data class Pelicula (val id: Int = 0, val titulo: Int = 0, val detalles: Int = 0,  val esFavorita: Boolean)

@Composable
fun ListaPeliculas(peliculas: List<Pelicula>, onToggleFavorito: (Int) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(19.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(items = peliculas, key = { p -> p.id }) { pelicula ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // El Modifier separa la distancia entre la información de la película y del botón de "Favorito"
                    Column (modifier = Modifier.weight(1f)) {
                        Text(text = stringResource(id = pelicula.titulo), style = MaterialTheme.typography.titleMedium)
                        Text(text = stringResource(id = pelicula.detalles), style = MaterialTheme.typography.bodySmall)
                    }
                    Modifier.weight(1f)
                    IconButton(onClick = { onToggleFavorito(pelicula.id) }) {
                        Icon(
                            imageVector = if (pelicula.esFavorita) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = stringResource(id = R.string.text_favorite),
                            tint = if (pelicula.esFavorita) Color.Red else Color.Gray
                        )
                    }
                }
            }
        }
    }
}