package ru.gorinih.androidacademy.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.presentation.ui.movies.paging.MoviesPagingSource

class MoviesRepository(private val moviesPaging: MoviesPagingSource) {

    fun loadMovies(): Flow<PagingData<Movies>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { moviesPaging }
        ).flow
    }
}