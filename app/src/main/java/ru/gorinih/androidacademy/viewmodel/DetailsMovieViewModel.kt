package ru.gorinih.androidacademy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gorinih.androidacademy.data.Movies
import ru.gorinih.androidacademy.data.repository.MoviesInteractor

class DetailsMovieViewModel(private val moviesInteractor: MoviesInteractor) : ViewModel() {

    private var _movie = MutableLiveData<Movies.Movie>()
    val movie: LiveData<Movies.Movie>
        get() = _movie

    fun getMovieById(id: Int) {
        viewModelScope.launch {
            val movieById = moviesInteractor.getMoviesById(id)
            _movie.value = movieById
        }
    }

}