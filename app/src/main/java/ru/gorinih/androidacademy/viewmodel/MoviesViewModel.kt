package ru.gorinih.androidacademy.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.gorinih.androidacademy.data.model.Movies
import ru.gorinih.androidacademy.data.repository.MoviesRepository
import kotlin.coroutines.CoroutineContext

class MoviesViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    private var page: Int = 0
    private val exceptionMovie =
        CoroutineExceptionHandler { coroutineContext: CoroutineContext, throwable: Throwable ->
            val isActive = coroutineContext.isActive
            Log.e(
                "MoviesViewModel::class",
                "ExceptionHandler [Scope active:$isActive, canceledContext:$throwable]"
            )
        }

    private var _movieList = MutableLiveData<List<Movies>>()
    val movieList: LiveData<List<Movies>>
        get() = _movieList

    init {
        nextMovies()
    }

    private fun getMoviesList(numberPage: Int) {
        viewModelScope.launch(exceptionMovie) {
            val listMovies = mutableListOf<Movies>()
            val oldMovies = movieList.value ?: listOf(Movies.Header)
            val listIdMovies = oldMovies.filterIsInstance(Movies.Movie::class.java).map { it.id }
            listMovies.addAll(oldMovies)
            val newMovies = moviesRepository.getMovies(numberPage, listIdMovies)
            if (newMovies.isNotEmpty()) {
                refreshCache(newMovies)
            }
            listMovies.addAll(newMovies)
            _movieList.value = listMovies
        }
    }

    private suspend fun refreshCache(listNewMovies: List<Movies.Movie>) {
        viewModelScope.launch(Dispatchers.IO + exceptionMovie) {
            val updateList = mutableListOf<Movies.Movie>()
            listNewMovies.forEach {
                val movie = moviesRepository.getMovieFromNetwork(it.id)
                if (movie != null) {
                    updateList.add(movie)
                }
            }
            if (updateList.count() > 0)
                moviesRepository.insertUpdateMovies(updateList)
        }
    }

    fun nextMovies() {
        page++
        getMoviesList(page)
    }

}