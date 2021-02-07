package ru.gorinih.androidacademy.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.gorinih.androidacademy.data.models.Genre
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.presentation.ui.movies.paging.MoviesRemoteMediator

class MoviesRepository(private val moviesMediator: MoviesRemoteMediator) {

    @ExperimentalPagingApi
    @OptIn(ExperimentalPagingApi::class)
    fun loadMovies(): Flow<PagingData<Movies.Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
            ),
            remoteMediator = moviesMediator,
            pagingSourceFactory = { moviesMediator.getMovies() }
        ).flow
    }

    suspend fun getGenres(id: Int): List<Genre> = withContext(Dispatchers.IO) {
        moviesMediator.loadGenres(id)
    }

}