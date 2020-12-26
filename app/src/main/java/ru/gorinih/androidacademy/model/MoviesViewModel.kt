package ru.gorinih.androidacademy.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gorinih.androidacademy.data.Movies
import ru.gorinih.androidacademy.repository.MoviesInteractor

class MoviesViewModel(private val moviesInteractor: MoviesInteractor) : ViewModel() {

    private var _movieList = MutableLiveData<List<Movies>>()
    val movieList: LiveData<List<Movies>>
        get() = _movieList

    private var _movie = MutableLiveData<Movies.Movie>()
    val movie: LiveData<Movies.Movie>
        get() = _movie

    init {
        getMoviesList()
    }

    private fun getMoviesList() {
        viewModelScope.launch {
            val movies = mutableListOf<Movies>(Movies.Header)
            val newMoviesList = moviesInteractor.getMovies()
            movies.addAll(newMoviesList)
            _movieList.value = movies
        }
    }

    private fun getMovieByIdInScope(id: Int) {
        viewModelScope.launch {
            val movieById = moviesInteractor.getMoviesById(id)
            _movie.value = movieById
        }
    }

    fun getMovieById(id: Int) {
        getMovieByIdInScope(id) // getMovieByIdInList(id)
    }

    // ARN
    private fun getMovieByIdInList(id: Int) {
        viewModelScope.launch {
            val movieById =
                _movieList.value!!.filterIsInstance(Movies.Movie::class.java).first { it.id == id }
            _movie.value = movieById
        }
    }

}