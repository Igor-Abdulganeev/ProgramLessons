package ru.gorinih.androidacademy.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gorinih.androidacademy.data.loadMovies
import ru.gorinih.androidacademy.data.model.Movies

class MovieStorage(private val context: Context) {
    suspend fun getMovies(): List<Movies.Movie> =
        withContext(Dispatchers.IO) {
            loadMovies(context)
        }

    suspend fun getMoviesById(id: Int): Movies.Movie =
        withContext(Dispatchers.IO) {
//            delay(1_000)
            loadMovies(context).first { it.id == id }
        }
}