package com.example.damflix.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.damflix.R
import com.example.damflix.viewmodel.MovieViewModel

@Composable
fun MovieForm(
    viewModel: MovieViewModel,
    onPeliculaGuardada: () -> Unit = {}
) {
    Spacer(modifier = Modifier.height(12.dp))

    var titulo by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var esVista by remember { mutableStateOf(false) }
    
    CampoEntrada(titulo, { titulo = it }, R.string.title_info, KeyboardType.Text)
    CampoEntrada(director, { director = it }, R.string.director_info, KeyboardType.Text)
    CampoEntrada(year, { year = it }, R.string.year_release_info, KeyboardType.Number)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 6.dp, end = 16.dp, bottom = 20.dp)
    ) {
        Checkbox(checked = esVista, onCheckedChange = { esVista = it })
        Text(text = stringResource(id = R.string.watched_movie))
    }

    Column(  // Tamaño normal pero centrado
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 100.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = {
                val anio = year.toIntOrNull() ?: 0
                viewModel.agregarPelicula(
                    titulo = titulo,
                    director = director,
                    anio = anio,
                    esVista = esVista
                )
                titulo = ""
                director = ""
                year = ""
                esVista = false
                onPeliculaGuardada()
            },
            enabled = titulo.isNotBlank() && director.isNotBlank() && year.toIntOrNull() != null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(id = R.string.save_movie))
        }
    }
}

@Composable
fun CampoEntrada(valor: String, onChange: (String) -> Unit, etiqueta: Int, keyboardType: KeyboardType) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(text = stringResource(id = etiqueta)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth().padding(start = 19.dp, top = 8.dp, end = 19.dp, bottom = 8.dp)
    )
}