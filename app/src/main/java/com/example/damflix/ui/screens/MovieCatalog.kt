package com.example.damflix.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.damflix.R
import com.example.damflix.viewmodel.MovieViewModel

@Composable
fun MovieCatalog(viewModel: MovieViewModel) {
    val peliculas by viewModel.listaPeliculas.collectAsStateWithLifecycle()
    
    LazyColumn(
        modifier = Modifier,
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
                        Text(text = pelicula.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(text = "${pelicula.director} • ${pelicula.anio}", style = MaterialTheme.typography.bodySmall)
                    }
                    Modifier.weight(1f)
                    IconButton(onClick = { viewModel.conmutarFavorito(pelicula) }) {
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