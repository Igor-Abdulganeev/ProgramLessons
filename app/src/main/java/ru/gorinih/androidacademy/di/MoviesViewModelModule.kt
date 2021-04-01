package ru.gorinih.androidacademy.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.gorinih.androidacademy.presentation.viewmodel.MovieViewModelFactory

@ExperimentalCoroutinesApi
@Module
abstract class MoviesViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: MovieViewModelFactory): ViewModelProvider.Factory
}