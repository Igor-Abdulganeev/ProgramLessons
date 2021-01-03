package ru.gorinih.androidacademy.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.gorinih.androidacademy.data.repository.MovieStorage
import ru.gorinih.androidacademy.data.repository.MoviesInteractor
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class DetailsMovieViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("IMPLICIT_CAST_TO_ANY")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        DetailsMovieViewModel::class.java -> {
            val movieStorage = MovieStorage(context)
            val moviesInteractor = MoviesInteractor(movieStorage)
            DetailsMovieViewModel(moviesInteractor)
        }
        else -> IllegalArgumentException("Not found DetailsMovieViewModel")
    } as T
}