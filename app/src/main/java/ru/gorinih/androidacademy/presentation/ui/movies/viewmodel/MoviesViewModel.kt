package ru.gorinih.androidacademy.presentation.ui.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.data.repository.MoviesRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MoviesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) :
    ViewModel() {

    private var currentMovies: Flow<PagingData<Movies>>? = null

    @OptIn(androidx.paging.ExperimentalPagingApi::class)
    fun getMovies(): Flow<PagingData<Movies>> {
        val previousMovies = currentMovies
        if (previousMovies != null) {
            return previousMovies
        }
        val newMovies: Flow<PagingData<Movies>> =
            moviesRepository.loadMovies()
                .cachedIn(viewModelScope)
        currentMovies = newMovies

        return newMovies
    }
}