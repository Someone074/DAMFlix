package com.example.damflix.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PeliculaDao {

    // Al retornar Flow, Room notifica automáticamente cualquier cambio en la tabla
    @Query("SELECT * FROM peliculas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<PeliculaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(pelicula: PeliculaEntity)

    @Update
    suspend fun actualizar(pelicula: PeliculaEntity)

    @Delete
    suspend fun eliminar(pelicula: PeliculaEntity)
}