package com.example.damflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.damflix.ui.theme.DAMFlixTheme

class MovieFormActivity : ComponentActivity() {
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
                        RegisterMovieForm()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RegisterMovieForm() {
    var titulo by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var esVista by remember { mutableStateOf(false) }

    CampoEntrada(titulo, { titulo = it }, R.string.title_info)
    CampoEntrada(director, { director = it }, R.string.director_info)
    CampoEntrada(year, { year = it }, R.string.year_release_info)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 6.dp, end = 16.dp, bottom = 20.dp)
    ) {
        Checkbox(checked = esVista, onCheckedChange = { esVista = it })
        Text(text = stringResource(id = R.string.watched_movie))
    }

    Column(  // Tamaño normal pero centrado
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = { /* Guardar película */ },
            enabled = titulo.isNotBlank() && director.isNotBlank(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(id = R.string.save_movie))
        }
    }
}

@Composable
fun CampoEntrada(valor: String, onCambio: (String) -> Unit, etiqueta: Int) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(text = stringResource(id = etiqueta)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth().padding(start = 19.dp, top = 8.dp, end = 19.dp, bottom = 8.dp)
    )
}