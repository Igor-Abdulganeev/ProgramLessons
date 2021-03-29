package ru.gorinih.androidacademy.presentation.ui.movies.paging

import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import ru.gorinih.androidacademy.data.db.MoviesRepoDatabase
import ru.gorinih.androidacademy.data.models.*
import ru.gorinih.androidacademy.data.network.MoviesNetwork
import java.io.InvalidObjectException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator @Inject constructor(
    private val moviesNetwork: MoviesNetwork,
    private val moviesRepoDatabase: MoviesRepoDatabase
) : RemoteMediator<Int, Movies>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movies>
    ): MediatorResult {
        val currentKey: Int = when (loadType) {
            LoadType.REFRESH -> {
                val keys = moviesRepoDatabase.loadRemoteKeyClosestToCurrentPosition(state)
                keys?.nextKey?.minus(1) ?: STARTING_PAGE
            }
            LoadType.APPEND -> {
                val keys = moviesRepoDatabase.loadRemoteKeyForLastItem(state)
                if (keys?.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                keys.nextKey
            }
            LoadType.PREPEND -> {
                val keys = moviesRepoDatabase.loadRemoteKeyForFirstItem(state)
                    ?: throw InvalidObjectException("Remote key and the prevKey should not be null")
                val prevKey = keys.prevKey
                    ?: return MediatorResult.Success(true)
                prevKey
            }
        }
        return try {
            val genres = moviesNetwork.getGenres()
            if (genres.isNotEmpty()) {
                withContext(Dispatchers.IO) { moviesRepoDatabase.insertGenres(genres) }
            }
            val result = moviesNetwork.getMoviesList(currentKey, genres)
            val endOfPaginationReached = result.isEmpty()
            if (!endOfPaginationReached) {
                moviesRepoDatabase.insertData(
                    currentKey,
                    endOfPaginationReached,
                    result,
                    loadType,
                    STARTING_PAGE
                )
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (ex: IOException) {
            MediatorResult.Error(ex)
        } catch (ex: HttpException) {
            MediatorResult.Error(ex)
        }
    }

    fun loadMovies(): PagingSource<Int, Movies> = moviesRepoDatabase.loadMovies()

    suspend fun loadGenres(idMovie: Int): List<Genre> = moviesRepoDatabase.loadGenres(idMovie)

    companion object {
        private const val STARTING_PAGE = 1
    }
}