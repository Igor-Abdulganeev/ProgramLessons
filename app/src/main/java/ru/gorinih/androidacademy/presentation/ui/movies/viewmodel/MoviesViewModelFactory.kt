package ru.gorinih.androidacademy.presentation.ui.movies.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.data.db.MoviesDatabase
import ru.gorinih.androidacademy.data.db.MoviesRepoDatabase
import ru.gorinih.androidacademy.data.network.MoviesApi
import ru.gorinih.androidacademy.data.network.MoviesNetwork
import ru.gorinih.androidacademy.data.repository.MoviesRepository
import ru.gorinih.androidacademy.presentation.ui.movies.paging.MoviesRemoteMediator

@FlowPreview
@InternalCoroutinesApi
@ExperimentalSerializationApi
@ExperimentalCoroutinesApi
class MoviesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesViewModel::class.java -> {
            val movieApi = MoviesApi.newInstance()
            val moviesNetwork = MoviesNetwork(moviesApi = movieApi)
            val movieDatabase = MoviesDatabase.newInstance(context)
            val moviesRepoDatabase = MoviesRepoDatabase(moviesDatabase = movieDatabase)
            val movieMediator = MoviesRemoteMediator(moviesNetwork, moviesRepoDatabase)
            val movieRepository = MoviesRepository(moviesMediator = movieMediator)
            MoviesViewModel(movieRepository)
        }
        else -> IllegalArgumentException("Not found MoviesViewModel")
    } as T
}