package ru.gorinih.androidacademy.services.di

import androidx.work.Worker
import dagger.Binds
import dagger.Module
import ru.gorinih.androidacademy.services.MoviesWorker

@Module
abstract class MoviesWorkerModule {

    @Binds
    abstract fun instanceWorker(work: MoviesWorker): Worker
}