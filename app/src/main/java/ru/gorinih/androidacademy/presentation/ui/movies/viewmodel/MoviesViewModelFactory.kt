package ru.gorinih.androidacademy.presentation.ui.movies.viewmodel

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
class MoviesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @ExperimentalSerializationApi
    @InternalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            val movieDB = MoviesDatabase.newInstance(context)
            val movieNetwork = MoviesNetwork(MoviesApi.newInstance())
            val movieRepository = MoviesRepository(movieDB.moviesDao, movieNetwork)
            return MoviesViewModel(movieRepository) as T
        }
        throw IllegalArgumentException("Not found MoviesViewModel")
    }
}