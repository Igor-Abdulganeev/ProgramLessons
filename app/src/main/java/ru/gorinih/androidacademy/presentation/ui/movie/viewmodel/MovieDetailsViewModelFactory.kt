package ru.gorinih.androidacademy.presentation.ui.movie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.data.network.MovieNetwork
import ru.gorinih.androidacademy.data.network.MoviesApi
import ru.gorinih.androidacademy.data.repository.MovieRepository
import java.lang.IllegalArgumentException

@ExperimentalSerializationApi
class MovieDetailsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MovieDetailsViewModel::class.java -> {
            val movieApi = MoviesApi.newInstance()
            val movieNetwork = MovieNetwork(movieApi)
            val movieRepository = MovieRepository(movieNetwork)
            MovieDetailsViewModel(movieRepository = movieRepository)
        }
        else -> IllegalArgumentException("Not found MovieViewModel")
    } as T
}