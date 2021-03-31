package ru.gorinih.androidacademy.presentation.ui.movie.di

import dagger.Subcomponent
import ru.gorinih.androidacademy.presentation.ui.movie.MovieDetailsFragment

@Subcomponent(modules = [MovieDetailsModule::class])
interface MovieDetailsSubComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MovieDetailsSubComponent
    }

    fun inject(fragment: MovieDetailsFragment)

}
