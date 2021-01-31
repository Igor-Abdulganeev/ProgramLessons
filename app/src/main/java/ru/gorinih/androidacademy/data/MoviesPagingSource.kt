package ru.gorinih.androidacademy.data

import androidx.paging.PagingSource
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.serialization.ExperimentalSerializationApi
import okio.IOException
import retrofit2.HttpException
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.data.network.MoviesNetwork

@ExperimentalSerializationApi
class MoviesPagingSource(
    private val moviesNetwork: MoviesNetwork
) : PagingSource<Int, Movies>() {
    @ExperimentalSerializationApi
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        val currentKey = params.key ?: STARTING_PAGE
        return try {
            val result = mutableListOf<Movies>()
            if (currentKey == STARTING_PAGE) {
                result.add(Movies.Header)
            }
            result.addAll(moviesNetwork.getMovies(currentKey))
            LoadResult.Page(
                data = result,
                prevKey = if (currentKey == STARTING_PAGE) null else currentKey,
                nextKey = if (result.count() > 0) currentKey + 1 else null
            )
        } catch (ex: IOException) {
            return LoadResult.Error(ex)
        } catch (ex: HttpException) {
            return LoadResult.Error(ex)
        }
    }


    companion object {
        private const val STARTING_PAGE = 1

        @Volatile
        private var moviesPSInstance: MoviesPagingSource? = null

        @InternalCoroutinesApi
        @ExperimentalSerializationApi
        fun newInstance(moviesNetwork: MoviesNetwork): MoviesPagingSource =
            moviesPSInstance ?: synchronized(this) {
                moviesPSInstance
                    ?: createMoviesPagingSource(moviesNetwork).also { moviesPSInstance = it }
            }

        private fun createMoviesPagingSource(moviesNetwork: MoviesNetwork): MoviesPagingSource {
            return MoviesPagingSource(moviesNetwork)
        }
    }
}