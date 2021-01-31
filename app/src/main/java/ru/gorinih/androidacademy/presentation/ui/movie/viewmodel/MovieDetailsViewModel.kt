package ru.gorinih.androidacademy.presentation.ui.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.data.repository.MoviesRepository

class MovieDetailsViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    private var _movie = MutableLiveData<Movies.Movie>()
    val movie: LiveData<Movies.Movie>
        get() = _movie

    @ExperimentalSerializationApi
    fun getMovie(idMove: Int) {
        viewModelScope.launch {
            val movie = moviesRepository.getMovie(idMove)
            if (movie != null) {
                _movie.value = movie
            }
        }
    }

}