package ru.gorinih.androidacademy.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.gorinih.androidacademy.presentation.ui.movie.viewmodel.MovieDetailsViewModel
import ru.gorinih.androidacademy.presentation.ui.movies.viewmodel.MoviesViewModel
import ru.gorinih.androidacademy.presentation.viewmodel.MovieViewModelFactory

@ExperimentalCoroutinesApi
@Module
abstract class MoviesViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindMoviesViewModel(moviesViewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindMovieDetailsViewModel(movieDetailsViewModel: MovieDetailsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: MovieViewModelFactory): ViewModelProvider.Factory
}