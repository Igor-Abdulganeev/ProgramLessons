package ru.gorinih.androidacademy.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.InternalCoroutinesApi
import ru.gorinih.androidacademy.data.db.MoviesDatabase
import ru.gorinih.androidacademy.data.repository.MoviesRepository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class MoviesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @InternalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            val movieDB = MoviesDatabase.newInstance(context)
            val movieRepository = MoviesRepository(movieDB.moviesDao)
            return MoviesViewModel(movieRepository) as T
        }
        throw IllegalArgumentException("Not found MoviesViewModel")
    }
}