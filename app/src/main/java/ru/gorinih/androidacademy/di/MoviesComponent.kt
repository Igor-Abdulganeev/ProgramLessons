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
import ru.gorinih.androidacademy.presentation.ui.movie.MovieDetailsFragment
import ru.gorinih.androidacademy.presentation.ui.movies.MoviesListFragment
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
@InternalCoroutinesApi
@Singleton
@Component(
    modules = [MoviesApiModule::class,
        MoviesDatabaseModule::class,
        MoviesViewModelModule::class]
)
interface MoviesComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MoviesComponent
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun inject(fragment: MoviesListFragment)
    fun inject(fragment: MovieDetailsFragment)

}