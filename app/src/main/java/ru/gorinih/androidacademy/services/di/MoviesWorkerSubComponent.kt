package ru.gorinih.androidacademy.services.di

import dagger.Subcomponent
import ru.gorinih.androidacademy.services.MoviesWorker

@Subcomponent(modules = [MoviesWorkerModule::class])
interface MoviesWorkerSubComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MoviesWorkerSubComponent
    }

    fun inject(value: MoviesWorker)
}