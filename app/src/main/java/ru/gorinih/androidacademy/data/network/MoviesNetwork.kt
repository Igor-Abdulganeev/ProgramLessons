package ru.gorinih.androidacademy.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import ru.gorinih.androidacademy.data.models.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesNetwork @Inject constructor(private val moviesApi: MoviesApi) {

    init {
        Log.d("ViewModel", "MoviesNetwork init")
    }

    suspend fun getGenres(): List<Genre> = withContext(Dispatchers.IO) {
        val genreTmdb = withContext(Dispatchers.IO) { moviesApi.getGenre() }
        return@withContext genreTmdb?.genres ?: emptyList()
    }

    suspend fun getMoviesList(currentKey: Int, genres: List<Genre>): List<Movies.Movie> {
        var conf: ConfigurationTmdb?
        var response: MoviesTmdb?
        coroutineScope {
            conf = withContext(Dispatchers.IO) { moviesApi.getConfiguration() }
            response = withContext(Dispatchers.IO) { moviesApi.getMovies(currentKey) }
        }
        return parseMovies(
            (response ?: emptyList<MoviesTmdb>()) as MoviesTmdb,
            genres,
            conf?.images?.secureBaseUrl ?: "http://image.tmdb.org/t/p/"
        )
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
}