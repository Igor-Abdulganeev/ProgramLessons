package ru.gorinih.androidacademy.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.gorinih.androidacademy.data.repository.MovieStorage
import ru.gorinih.androidacademy.data.repository.MoviesInteractor
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class MoviesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            val movieStorage = MovieStorage(context)
            val movieInteractor = MoviesInteractor(movieStorage)
            return MoviesViewModel(movieInteractor) as T
        }
        throw IllegalArgumentException("Not found MoviesViewModel")
    }
}