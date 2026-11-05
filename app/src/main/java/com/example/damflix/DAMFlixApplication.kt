package com.example.damflix

import android.app.Application
import com.example.damflix.model.MovieRepository
import com.example.damflix.model.AppDatabase

class DAMFlixApplication : Application() {

    // Solo se crea la BD cuando se acceda a ella por primera vez
    val modelbase by lazy { AppDatabase.getDatabase(this) }

    // Se crea el repositorio cuando se necesite, recibiendo el DAO de la BD perezosa
    val repository by lazy { MovieRepository(modelbase.peliculaDao()) }
}