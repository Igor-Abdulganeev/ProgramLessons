package ru.gorinih.androidacademy.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import ru.gorinih.androidacademy.data.models.*

@Database(
    entities = [Movies.Movie::class,
        Actor::class,
        Genre::class,
        RelationActorsOfMovie::class,
        RelationGenresOfMovie::class,
        RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class MoviesDatabase : RoomDatabase() {

    abstract val moviesDao: MoviesDao
    abstract val remoteKeysDao: RemoteKeysDao

    companion object {
        private var hInstance: MoviesDatabase? = null

        @InternalCoroutinesApi
        fun newInstance(context: Context) = hInstance ?: synchronized(this) {
            hInstance ?: buildMoviesDatabase(context).also { hInstance = it }
        }

        private fun buildMoviesDatabase(context: Context): MoviesDatabase = Room.databaseBuilder(
            context.applicationContext,
            MoviesDatabase::class.java,
            "movies_db"
        ).fallbackToDestructiveMigration()
            .build()
    }
}
