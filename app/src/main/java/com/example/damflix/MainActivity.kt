package com.example.damflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.damflix.ui.theme.DAMFlixTheme

class MainActivity : ComponentActivity() {
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
                        StartScreen()
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
        path = "formulario",
        label = "Añadir",
        icon = Icons.Filled.Add
    )
}

@Preview
@Composable
fun StaticMovie() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(19.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.poster_inception),
            contentDescription = stringResource(R.string.desc_poster),
            modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = stringResource(id = R.string.movie_name), style = MaterialTheme.typography.titleMedium)
            Text(text = stringResource(id = R.string.movie_details), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview
@Composable
fun UserHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_avatar),
            contentDescription = stringResource(R.string.avatar_desc),
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape) // Avatar circular
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = stringResource(id = R.string.user_name_label),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(id = R.string.user_rol),
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = stringResource(id = R.string.user_stadistics),
                style = MaterialTheme.typography.bodySmall
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.name_start),
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
        bottomBar = {
            // Ruta actual del BackStack
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val rutaActual = navBackStackEntry?.destination?.route

            NavigationBar {
                // Lista inmutable con las rutas
                listOf(
                    Ruta.Listado,
                    Ruta.Inicio,
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
            composable(Ruta.Listado.path) {
                Column{
                    // Agregación de cada pantalla por medio de un Composable de cada Activity.
                    // No se agregan activities directamente (es un error).
                    CatalogScreen()
                }
            }
            composable(Ruta.Inicio.path) {
                Column{
                    // Espacio pequeño justo debajo de la TopBar
                    Spacer(modifier = Modifier.height(12.dp))

                    UserHeader()
                    StaticMovie()
                }
            }
            composable(Ruta.Formulario.path) {
                Column{
                    Spacer(modifier = Modifier.height(12.dp))
                    RegisterMovieForm()
                }
            }
        }
    }
}
