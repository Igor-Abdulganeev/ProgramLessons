package ru.gorinih.androidacademy.di

import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.presentation.ui.movie.di.MovieDetailsSubComponent
import ru.gorinih.androidacademy.presentation.ui.movies.di.MoviesSubComponent
import ru.gorinih.androidacademy.services.di.MoviesWorkerSubComponent
import javax.inject.Singleton

@FlowPreview
@ExperimentalSerializationApi
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module(subcomponents = [MovieDetailsSubComponent::class, MoviesSubComponent::class, MoviesWorkerSubComponent::class])
class MoviesSubcomponents {
}