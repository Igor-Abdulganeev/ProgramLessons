package ru.gorinih.androidacademy.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.data.di.MoviesApiModule
import ru.gorinih.androidacademy.data.di.MoviesDatabaseModule
import ru.gorinih.androidacademy.presentation.MainActivity
import ru.gorinih.androidacademy.presentation.ui.movie.di.MovieDetailsSubComponent
import ru.gorinih.androidacademy.presentation.ui.movies.MoviesListFragment
import ru.gorinih.androidacademy.presentation.ui.movies.di.MoviesSubComponent
import ru.gorinih.androidacademy.services.MoviesWorker
import ru.gorinih.androidacademy.services.di.MoviesWorkerSubComponent
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
@InternalCoroutinesApi
@FlowPreview
@Singleton
@Component(
    modules = [MoviesApiModule::class,
        MoviesDatabaseModule::class,
        MoviesViewModelModule::class,
        MoviesSubcomponents::class]
)
interface MoviesComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MoviesComponent
    }

    fun registrationMovieDetailsSubcomponent(): MovieDetailsSubComponent.Factory

    fun registrationMoviesListSubcomponent(): MoviesSubComponent.Factory

    fun registrationMoviesWorker(): MoviesWorkerSubComponent.Factory

    fun inject(activity: MainActivity)
}