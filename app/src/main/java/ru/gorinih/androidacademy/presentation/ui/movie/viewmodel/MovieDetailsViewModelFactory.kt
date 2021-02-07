package ru.gorinih.androidacademy.presentation.ui.movie.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.data.db.MoviesDatabase
import ru.gorinih.androidacademy.data.network.MoviesApi
import ru.gorinih.androidacademy.data.repository.MovieRepository

@ExperimentalSerializationApi
@InternalCoroutinesApi
class MovieDetailsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MovieDetailsViewModel::class.java -> {
            val movieApi = MoviesApi.newInstance()
            val moviesDatabase = MoviesDatabase.newInstance(context)
            val movieRepository = MovieRepository(movieApi, moviesDatabase)
            MovieDetailsViewModel(movieRepository = movieRepository)
        }
        else -> IllegalArgumentException("Not found MovieViewModel")
    } as T
}