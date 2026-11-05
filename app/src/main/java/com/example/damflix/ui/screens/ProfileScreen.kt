package com.example.damflix.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.damflix.R
import com.example.damflix.viewmodel.MovieViewModel

@Composable
fun ProfileScreen(viewModel: MovieViewModel) {
    // Consigue la lista de películas
    val peliculas by viewModel.listaPeliculas.collectAsStateWithLifecycle()

    // Cuenta las películas vistas
    val nVistas = remember(peliculas) { peliculas.count { it.esVista } }

    // Cuenta las películas favoritas
    val nFavoritas = remember(peliculas) { peliculas.count { it.esFavorita } }

    // UserHeader
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 40.dp, top = 40.dp, end = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.user_name_label),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
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
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.user_avatar),
            contentDescription = stringResource(R.string.avatar_desc),
            modifier = Modifier
                .size(115.dp)
                .clip(CircleShape) // Avatar circular
        )
    }

    // StaticMovie
    /*
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
    */
}