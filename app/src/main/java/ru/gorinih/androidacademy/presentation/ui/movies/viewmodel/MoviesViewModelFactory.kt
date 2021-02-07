package ru.gorinih.androidacademy.presentation.ui.movies.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.data.db.MoviesDatabase
import ru.gorinih.androidacademy.data.repository.MoviesRepository
import ru.gorinih.androidacademy.data.network.MoviesApi
import ru.gorinih.androidacademy.presentation.ui.movies.paging.MoviesRemoteMediator
import java.lang.IllegalArgumentException

@FlowPreview
@InternalCoroutinesApi
@ExperimentalSerializationApi
class MoviesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesViewModel::class.java -> {
            val movieApi = MoviesApi.newInstance()
            val movieDatabase = MoviesDatabase.newInstance(context)
//            val moviePaging = MoviesPagingSourc(movieDatabase)
            val movieMediator = MoviesRemoteMediator(movieApi, movieDatabase)
            val movieRepository = MoviesRepository(moviesMediator = movieMediator)
            MoviesViewModel(movieRepository)
        }
        else -> IllegalArgumentException("Not found MoviesViewModel")
    } as T
}