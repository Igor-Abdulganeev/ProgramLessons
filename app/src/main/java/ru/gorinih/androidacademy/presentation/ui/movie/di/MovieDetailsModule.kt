package ru.gorinih.androidacademy.presentation.ui.movie.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.gorinih.androidacademy.di.ViewModelKey
import ru.gorinih.androidacademy.presentation.ui.movie.viewmodel.MovieDetailsViewModel

@Module
abstract class MovieDetailsModule {
    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindMovieDetailsViewModel(movieDetailsViewModel: MovieDetailsViewModel): ViewModel
}