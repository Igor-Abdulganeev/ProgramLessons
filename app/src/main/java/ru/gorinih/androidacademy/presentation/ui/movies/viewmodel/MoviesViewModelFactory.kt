package ru.gorinih.androidacademy.presentation.ui.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.data.repository.MoviesRepository
import ru.gorinih.androidacademy.data.network.MoviesApi
import ru.gorinih.androidacademy.presentation.ui.movies.paging.MoviesPagingSource
import java.lang.IllegalArgumentException

@FlowPreview
@InternalCoroutinesApi
@ExperimentalSerializationApi
class MoviesViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesViewModel::class.java -> {
            val movieApi = MoviesApi.newInstance()
            val moviePaging = MoviesPagingSource(movieApi)
            val movieRepository = MoviesRepository(moviesPaging = moviePaging)
            MoviesViewModel(movieRepository)
        }
        else -> IllegalArgumentException("Not found MoviesViewModel")
    } as T
}