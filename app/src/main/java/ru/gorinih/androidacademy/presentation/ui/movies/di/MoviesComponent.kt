package ru.gorinih.androidacademy.presentation.ui.movies.di

import android.content.Context
import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.data.di.MoviesApiModule
import ru.gorinih.androidacademy.data.di.MoviesDatabaseModule
import ru.gorinih.androidacademy.presentation.MainActivity
import ru.gorinih.androidacademy.presentation.ui.movies.MoviesListFragment

@Component(modules = [MoviesApiModule::class, MoviesDatabaseModule::class])
@InternalCoroutinesApi
@FlowPreview
@ExperimentalSerializationApi
@ExperimentalCoroutinesApi
interface MoviesComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MoviesComponent
    }

    fun inject(fragment: MoviesListFragment)
}