package ru.gorinih.androidacademy.presentation.ui.movies.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.gorinih.androidacademy.di.ViewModelKey
import ru.gorinih.androidacademy.presentation.ui.movies.viewmodel.MoviesViewModel

@ExperimentalCoroutinesApi
@Module
abstract class MoviesModule {
    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindMoviesViewModel(moviesViewModel: MoviesViewModel): ViewModel
}
