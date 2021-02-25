package ru.gorinih.androidacademy.data.repository

import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.gorinih.androidacademy.data.models.Genre
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.presentation.ui.movies.paging.MoviesRemoteMediator

class MoviesRepository(private val moviesMediator: MoviesRemoteMediator) {

    @ExperimentalPagingApi
    @OptIn(ExperimentalPagingApi::class)
    fun loadMovies(): Flow<PagingData<Movies>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
            ),
            remoteMediator = moviesMediator,
            pagingSourceFactory = { moviesMediator.loadMovies() }
        ).flow
            .map { pagingData ->
                pagingData.map {
                    it as Movies.Movie
                    val genres = getGenres(it.id)
                    it.listOfGenre = genres
                    it
                }
            }
            .map {
                it.insertSeparators<Movies.Movie, Movies> { before, _ ->
                    if (before == null) {
                        Movies.Header
                    } else {
                        null
                    }
                }
            }
    }

    private suspend fun getGenres(id: Int): List<Genre> = withContext(Dispatchers.IO) {
        moviesMediator.loadGenres(id)
    }

}