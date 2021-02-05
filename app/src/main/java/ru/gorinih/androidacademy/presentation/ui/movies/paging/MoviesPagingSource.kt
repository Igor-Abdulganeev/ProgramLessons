package ru.gorinih.androidacademy.presentation.ui.movies.paging

import androidx.paging.PagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import ru.gorinih.androidacademy.data.models.*
import ru.gorinih.androidacademy.data.network.MoviesApi

class MoviesPagingSource(private val moviesApi: MoviesApi) :
    PagingSource<Int, Movies>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        val currentKey = params.key ?: STARTING_PAGE
        return try {
            var conf: ConfigurationTmdb?
            var response: MoviesTmdb?
            var genreTmdb: GenreTmdb?
            coroutineScope {
                conf = withContext(Dispatchers.IO) { moviesApi.getConfiguration() }
                response = withContext(Dispatchers.IO) { moviesApi.getMovies(currentKey) }
                genreTmdb = withContext(Dispatchers.IO) { moviesApi.getGenre() }
            }
            val resultMutable = mutableListOf<Movies>()
            if (currentKey == STARTING_PAGE) {
                resultMutable.add(Movies.Header)
            }
            val result = parseMovies(
                (response ?: emptyList<MoviesTmdb>()) as MoviesTmdb,
                genreTmdb?.genres ?: emptyList(),
                conf?.images?.secureBaseUrl ?: "http://image.tmdb.org/t/p/"
            )
            resultMutable.addAll(result)
            LoadResult.Page(
                data = resultMutable,
                prevKey = if (currentKey == STARTING_PAGE) null else currentKey,
                nextKey = if (result.isNotEmpty()) currentKey + 1 else null
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    private fun parseMovies(
        data: MoviesTmdb,
        genreTmdb: List<Genre>,
        baseImageUrl: String
    ): List<Movies> {
        val genres = genreTmdb.associateBy { it.id }
        return data.results.map { it ->
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