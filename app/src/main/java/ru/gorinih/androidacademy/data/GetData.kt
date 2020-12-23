package ru.gorinih.androidacademy.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gorinih.androidacademy.model.Movies

class GetData {
    suspend fun getMovies(context: Context): List<Movies.Movie> =
        withContext(Dispatchers.IO) {
            loadMovies(context)
        }

    suspend fun getMoviesById(context: Context, id: Int): Movies.Movie =
        withContext(Dispatchers.IO) {
            loadMovies(context).first { it.id == id }
        }
}