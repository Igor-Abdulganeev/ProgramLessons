package ru.gorinih.androidacademy.presentation.ui.movies.di

import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.di.FragmentScope
import ru.gorinih.androidacademy.presentation.ui.movies.MoviesListFragment

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ExperimentalSerializationApi
@FlowPreview
@Subcomponent(modules = [MoviesModule::class])
interface MoviesSubComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MoviesSubComponent
    }

    fun inject(fragment: MoviesListFragment)
}