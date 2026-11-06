package com.example.damflix.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.damflix.R

@Composable
fun StartScreen() {
    Spacer(modifier = Modifier.height(12.dp))

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(19.dp),
        horizontalArrangement = Arrangement.spacedBy(19.dp),
        verticalArrangement = Arrangement.spacedBy(19.dp)
    ) {
        item { CaratulaPelicula(R.drawable.poster_inception, R.string.desc_inception) }
        item { CaratulaPelicula(R.drawable.poster_yourname, R.string.desc_yourname) }
        item { CaratulaPelicula(R.drawable.poster_alicia, R.string.desc_alicia) }
        item { CaratulaPelicula(R.drawable.poster_harrypotter, R.string.desc_harrypotter) }
        item { CaratulaPelicula(R.drawable.poster_carrie, R.string.desc_carrie) }
        item { CaratulaPelicula(R.drawable.poster_cuartafase, R.string.desc_cuartafase) }
    }
}

@Composable
fun CaratulaPelicula(caratula: Int, descripcion: Int) {
    Image(
        painter = painterResource(id = caratula),
        contentDescription = stringResource(descripcion),
        modifier = Modifier.clip(RoundedCornerShape(10.dp))
    )
}