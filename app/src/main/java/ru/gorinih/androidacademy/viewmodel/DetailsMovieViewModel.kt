package ru.gorinih.androidacademy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gorinih.androidacademy.data.model.Movies
import ru.gorinih.androidacademy.data.repository.MoviesRepository

class DetailsMovieViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    private var _movie = MutableLiveData<Movies.Movie>()
    val movie: LiveData<Movies.Movie>
        get() = _movie

    fun getMovie(idMove: Int) {
        viewModelScope.launch {
            val movie = moviesRepository.getMovie(idMove)
            if (movie != null) {
                _movie.value = movie
            }
        }
    }

}