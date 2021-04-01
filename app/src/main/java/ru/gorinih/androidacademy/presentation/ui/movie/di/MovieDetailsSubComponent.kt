package ru.gorinih.androidacademy.presentation.ui.movie.di

import dagger.Subcomponent
import ru.gorinih.androidacademy.di.FragmentScope
import ru.gorinih.androidacademy.presentation.ui.movie.MovieDetailsFragment

@FragmentScope
@Subcomponent(modules = [MovieDetailsModule::class])
interface MovieDetailsSubComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MovieDetailsSubComponent
    }

    fun inject(fragment: MovieDetailsFragment)

}
