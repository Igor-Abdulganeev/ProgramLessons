package ru.gorinih.androidacademy.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.gorinih.androidacademy.repository.MoviesInteractor
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class MoviesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            val movieInteractor = MoviesInteractor(context)
            return MoviesViewModel(movieInteractor) as T
        }
        throw IllegalArgumentException("Not found MoviesViewModel")
    }
}