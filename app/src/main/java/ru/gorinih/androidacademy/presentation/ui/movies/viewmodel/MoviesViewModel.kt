package ru.gorinih.androidacademy.presentation.ui.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.data.repository.MoviesRepository

@ExperimentalCoroutinesApi
class MoviesViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    private var currentMovies: Flow<PagingData<Movies>>? = null

    @OptIn(androidx.paging.ExperimentalPagingApi::class)
    fun getMovies(): Flow<PagingData<Movies>> {
        val previousMovies = currentMovies
        if (previousMovies != null) {
            return previousMovies
        }
        val newMovies: Flow<PagingData<Movies>> =
            moviesRepository.loadMovies()
/*
                .mapLatest {
                    it.insertSeparators { befor, after ->
                        if (befor.let {
                                (it as Movies.Movie).idDb
                            } == 1) {
                            Movies.Header
                        } else {
                            null
                        }
                    }
                }
*/
/*
                .transform {
                    emit(PagingData.from(listOf(Movies.Header) as List<Movies>))
                }
*/
                .cachedIn(viewModelScope)
        currentMovies = newMovies

        return newMovies
    }
}