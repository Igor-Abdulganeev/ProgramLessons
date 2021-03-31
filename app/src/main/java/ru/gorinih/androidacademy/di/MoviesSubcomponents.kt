package ru.gorinih.androidacademy.di

import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.presentation.ui.movie.di.MovieDetailsSubComponent

@ExperimentalSerializationApi
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module(subcomponents = [MovieDetailsSubComponent::class])
class MoviesSubcomponents {
}