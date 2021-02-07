package ru.gorinih.androidacademy.presentation.ui.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.data.repository.MoviesRepository

class MoviesViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    private var currentMovies: Flow<PagingData<Movies>>? = null

    @OptIn(androidx.paging.ExperimentalPagingApi::class)
    fun getMovies(): Flow<PagingData<Movies>> {
        val previousMovies = currentMovies
        if (previousMovies != null) {
            return previousMovies
        }
        val newMovies: Flow<PagingData<Movies>> = moviesRepository.loadMovies()
            .map { pagingData ->
                pagingData.map {
                    val genres = moviesRepository.getGenres(it.id)
                    it.listOfGenre = genres
                    it
                }
            }
        //cachedIn(viewModelScope)

        currentMovies = newMovies
        return newMovies
    }
}