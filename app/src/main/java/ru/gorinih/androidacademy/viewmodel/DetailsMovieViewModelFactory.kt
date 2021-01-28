package ru.gorinih.androidacademy.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.data.db.MoviesDatabase
import ru.gorinih.androidacademy.data.network.MoviesApi
import ru.gorinih.androidacademy.data.network.MoviesNetwork
import ru.gorinih.androidacademy.data.repository.MoviesRepository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class DetailsMovieViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @ExperimentalSerializationApi
    @InternalCoroutinesApi
    @Suppress("IMPLICIT_CAST_TO_ANY")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        DetailsMovieViewModel::class.java -> {
            val movieDB = MoviesDatabase.newInstance(context)
            val movieNetwork = MoviesNetwork(MoviesApi.newInstance())
            val moviesRepository = MoviesRepository(movieDB.moviesDao, movieNetwork)
            DetailsMovieViewModel(moviesRepository)
        }
        else -> IllegalArgumentException("Not found DetailsMovieViewModel")
    } as T
}