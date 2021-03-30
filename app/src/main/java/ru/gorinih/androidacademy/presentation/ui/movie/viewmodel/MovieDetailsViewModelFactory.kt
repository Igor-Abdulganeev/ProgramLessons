package ru.gorinih.androidacademy.presentation.ui.movie.viewmodel
/*

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.gorinih.androidacademy.data.repository.MovieRepository
import java.lang.IllegalArgumentException
import javax.inject.Inject

class MovieDetailsViewModelFactory @Inject constructor(): ViewModelProvider.Factory {
    @Inject
    lateinit var movieRepository: MovieRepository
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            MovieDetailsViewModel(movieRepository = movieRepository) as T
        } else IllegalArgumentException("Not found MovieDetailsViewModel") as T
    }
}*/
