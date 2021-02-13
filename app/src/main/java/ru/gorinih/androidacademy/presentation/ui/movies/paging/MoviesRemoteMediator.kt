package ru.gorinih.androidacademy.presentation.ui.movies.paging

import androidx.paging.*
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import ru.gorinih.androidacademy.data.db.MoviesDatabase
import ru.gorinih.androidacademy.data.models.*
import ru.gorinih.androidacademy.data.network.MoviesApi
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val moviesApi: MoviesApi,
    private val moviesDatabase: MoviesDatabase
) : RemoteMediator<Int, Movies>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movies>
    ): MediatorResult {
        val currentKey: Int = when (loadType) {
            LoadType.REFRESH -> {
                val keys = loadRemoteKeyClosestToCurrentPosition(state)
                keys?.nextKey?.minus(1) ?: STARTING_PAGE
            }
            LoadType.APPEND -> {
                val keys = loadRemoteKeyForLastItem(state)
                if (keys?.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                keys.nextKey
            }
            LoadType.PREPEND -> {
                val keys = loadRemoteKeyForFirstItem(state)
                    ?: throw InvalidObjectException("Remote key and the prevKey should not be null")
                val prevKey = keys.prevKey
                    ?: return MediatorResult.Success(true)
                prevKey
            }
        }
        return try {
            val result = getData(currentKey)
            val endOfPaginationReached = result.isEmpty()
            if (!endOfPaginationReached) {
                moviesDatabase.withTransaction {
                    //                clearDatabase(loadType)
                    insertData(currentKey, endOfPaginationReached, result)
                }
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (ex: IOException) {
            MediatorResult.Error(ex)
        } catch (ex: HttpException) {
            MediatorResult.Error(ex)
        }
    }

    suspend fun insertData(
        currentKey: Int,
        endOfPaginationReached: Boolean,
        result: List<Movies.Movie>
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
                    insertGenres(it.id, it.listOfGenre)
                }
            }
        }

    }

    suspend fun getData(currentKey: Int): List<Movies.Movie> {
        var conf: ConfigurationTmdb?
        var response: MoviesTmdb?
        var genreTmdb: GenreTmdb?
        coroutineScope {
            conf = withContext(Dispatchers.IO) { moviesApi.getConfiguration() }
            response = withContext(Dispatchers.IO) { moviesApi.getMovies(currentKey) }
            genreTmdb = withContext(Dispatchers.IO) { moviesApi.getGenre() }
        }
        val genres: List<Genre> = genreTmdb?.genres ?: emptyList()
        if (genres.isNotEmpty()) {
            withContext(Dispatchers.IO) { insertAllGenres(genres) }
        }
        return parseMovies(
            (response ?: emptyList<MoviesTmdb>()) as MoviesTmdb,
            genres,
            conf?.images?.secureBaseUrl ?: "http://image.tmdb.org/t/p/"
        )
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

    private suspend fun insertGenres(id: Int, listOfGenre: List<Genre>) {
        val items: List<RelationGenresOfMovie> = listOfGenre.map {
            RelationGenresOfMovie(
                id = 0,
                movie_id = id,
                genre_id = it.id
            )
        }
        withContext(Dispatchers.IO) { moviesDatabase.moviesDao.insertGenresOfMovie(items) }
    }

    private suspend fun insertAllGenres(listOfGenre: List<Genre>) = withContext(Dispatchers.IO) {
        moviesDatabase.moviesDao.insertGenres(items = listOfGenre)
    }

    private suspend fun loadRemoteKeyForLastItem(loadState: PagingState<Int, Movies>): RemoteKeys? {
        return loadState.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let {
            moviesDatabase.remoteKeysDao.remoteKeysById((it as Movies.Movie).id)
        }
    }

    private suspend fun loadRemoteKeyForFirstItem(loadState: PagingState<Int, Movies>): RemoteKeys? {
        return loadState.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let {
            moviesDatabase.remoteKeysDao.remoteKeysById((it as Movies.Movie).id)
        }
    }

    private suspend fun loadRemoteKeyClosestToCurrentPosition(loadState: PagingState<Int, Movies>): RemoteKeys? {
        return loadState.anchorPosition?.let {
            (loadState.closestItemToPosition(it) as Movies.Movie).id.let { lastIt ->
                moviesDatabase.remoteKeysDao.remoteKeysById(lastIt)
            }
        }
    }

    private fun parseMovies(
        data: MoviesTmdb,
        genreTmdb: List<Genre>,
        baseImageUrl: String
    ): List<Movies.Movie> {
        val genres = genreTmdb.associateBy { it.id }
        return data.results.map {
            Movies.Movie(
                id = it.id,
                nameMovie = it.originalTitle,
                description = it.overview ?: "",
                poster = it.posterPath.let { poster -> "${baseImageUrl}w342${poster}" },
                detailPoster = it.backdropPath.let { poster -> "${baseImageUrl}w500${poster}" },
                rating = (it.voteAverage ?: 0.0F).toFloat(),
                reviews = it.voteCount ?: 0,
                rated = if (it.adult == true) "+16" else "+13",
                duration = 0,
                likes = false,
                listOfGenre = it.genreIds?.map { id ->
                    genres[id] ?: Genre(0, "")
                } ?: emptyList(),
                listOfActors = emptyList()
            )
        }
    }

    companion object {
        private const val STARTING_PAGE = 1
    }
}