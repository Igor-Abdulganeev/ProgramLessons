package ru.gorinih.androidacademy.presentation.ui.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.data.repository.MovieRepository

class MovieDetailsViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private var _movie = MutableLiveData<Movies.Movie>()
    val movie: LiveData<Movies.Movie>
        get() = _movie

    @ExperimentalStdlibApi
    fun getMovie(idMove: Int) {
        viewModelScope.launch {
            val movie: Movies.Movie? = movieRepository.loadMovie(idMove)
            movie?.let {
                _movie.value = movie
                if (movie.listOfActors.isEmpty()) {
                    val newMovie: Movies.Movie? = movieRepository.loadMovieNetwork(idMove)
                    newMovie?.let { _movie.value = newMovie }
                }
            }
        }
    }

}