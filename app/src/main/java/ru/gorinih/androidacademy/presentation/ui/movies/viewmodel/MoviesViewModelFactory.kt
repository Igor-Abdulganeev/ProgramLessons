package ru.gorinih.androidacademy.presentation.ui.movies.viewmodel
/*

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.gorinih.androidacademy.data.repository.MoviesRepository
import javax.inject.Inject

class MoviesViewModelFactory @Inject constructor(): ViewModelProvider.Factory {
    @Inject lateinit var moviesRepository: MoviesRepository
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass){
        MoviesViewModel::class.java -> {
        MoviesViewModel(moviesRepository = moviesRepository)
    }
        else -> IllegalArgumentException("Not found MoviesViewModel")
    } as T
}*/
