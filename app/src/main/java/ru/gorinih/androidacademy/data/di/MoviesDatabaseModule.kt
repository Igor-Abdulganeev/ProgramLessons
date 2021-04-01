package ru.gorinih.androidacademy.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi
import ru.gorinih.androidacademy.data.db.MoviesDatabase
import javax.inject.Singleton

@InternalCoroutinesApi
@Module
class MoviesDatabaseModule() {
    @Singleton
    @Provides
    fun provideDatabase(context: Context): MoviesDatabase =
        MoviesDatabase.newInstance(context)
}