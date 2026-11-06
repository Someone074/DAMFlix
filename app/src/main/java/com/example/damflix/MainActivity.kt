package com.example.damflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.damflix.ui.navigation.AppDAMFlixMainScreen
import com.example.damflix.viewmodel.MovieViewModel
import com.example.damflix.ui.theme.DAMFlixTheme
import com.example.damflix.viewmodel.MovieViewModelFactory

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
                        // Obtención/Mantenimiento del ViewModel (retenido ante rotaciones mediante la fábrica)
                        val viewModel: MovieViewModel = viewModel(factory = factory)

                        // Pantalla principal con el ViewModel
                        AppDAMFlixMainScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

