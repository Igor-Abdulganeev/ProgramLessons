package ru.gorinih.androidacademy.data.db

import android.util.Log
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import ru.gorinih.androidacademy.data.models.Genre
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.data.models.RelationGenresOfMovie
import ru.gorinih.androidacademy.data.models.RemoteKeys
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepoDatabase @Inject constructor(private val moviesDatabase: MoviesDatabase) {

    init {
        Log.d("ViewModel", "RepoDatabase init")
    }

    suspend fun insertData(
        currentKey: Int,
        endOfPaginationReached: Boolean,
        result: List<Movies.Movie>,
        loadType: LoadType,
        STARTING_PAGE: Int
    ) {
        moviesDatabase.withTransaction {
            //clearDatabase(loadType)
            insertMovies(currentKey, endOfPaginationReached, result, STARTING_PAGE)
        }
    }

    private suspend fun insertMovies(
        currentKey: Int,
        endOfPaginationReached: Boolean,
        result: List<Movies.Movie>,
        STARTING_PAGE: Int
    ) {
        val prevKey = if (currentKey == STARTING_PAGE) null else currentKey - 1
        val nextKey = if (endOfPaginationReached) null else currentKey + 1
        val keys = result.map { RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey) }
        coroutineScope {
            withContext(Dispatchers.IO) {
                moviesDatabase.remoteKeysDao.insertRemoteKeys(remoteKey = keys)
            }
            withContext(Dispatchers.IO) {
                moviesDatabase.moviesDao.insertUpdateMovies(items = result)
            }
            withContext(Dispatchers.IO) {
                result.forEach {
                    insertGenresOfMovie(it.id, it.listOfGenre)
                }
            }
        }
    }

    private suspend fun clearDatabase(loadType: LoadType) {
        if (loadType == LoadType.REFRESH) {
            coroutineScope {
                withContext(Dispatchers.IO) { moviesDatabase.remoteKeysDao.clearRemoteKeys() }
                withContext(Dispatchers.IO) { moviesDatabase.moviesDao.clearMovies() }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun loadMovies(): PagingSource<Int, Movies> {
        val movies = moviesDatabase.moviesDao.loadMoviesFromDB()
        return movies as PagingSource<Int, Movies>
    }

    suspend fun loadGenres(idMovie: Int): List<Genre> = withContext(Dispatchers.IO) {
        moviesDatabase.moviesDao.loadGenresById(idMovie) ?: emptyList()
    }

    private suspend fun insertGenresOfMovie(id: Int, listOfGenre: List<Genre>) {
        val items: List<RelationGenresOfMovie> = listOfGenre.map {
            RelationGenresOfMovie(
                id = 0,
                movie_id = id,
                genre_id = it.id
            )
        }
        withContext(Dispatchers.IO) { moviesDatabase.moviesDao.insertGenresOfMovie(items) }
    }

    suspend fun insertGenres(listOfGenre: List<Genre>) = withContext(Dispatchers.IO) {
        moviesDatabase.moviesDao.insertGenres(items = listOfGenre)
    }

    suspend fun loadRemoteKeyForLastItem(loadState: PagingState<Int, Movies>): RemoteKeys? {
        return loadState.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let {
            moviesDatabase.remoteKeysDao.remoteKeysById((it as Movies.Movie).id)
        }
    }

    suspend fun loadRemoteKeyForFirstItem(loadState: PagingState<Int, Movies>): RemoteKeys? {
        return loadState.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let {
            moviesDatabase.remoteKeysDao.remoteKeysById((it as Movies.Movie).id)
        }
    }

    suspend fun loadRemoteKeyClosestToCurrentPosition(loadState: PagingState<Int, Movies>): RemoteKeys? {
        return loadState.anchorPosition?.let {
            (loadState.closestItemToPosition(it) as Movies.Movie).id.let { lastIt ->
                moviesDatabase.remoteKeysDao.remoteKeysById(lastIt)
            }
        }
    }

    fun getMaxNextKey() = moviesDatabase.remoteKeysDao.getMaxNextKey()
}