package ru.gorinih.androidacademy.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.data.model.*
import java.io.IOException


@ExperimentalSerializationApi
class MoviesNetwork(private val moviesApi: MoviesApi) {

    private fun handleCallError(throwable: Throwable) {
        when (throwable) {
            is IOException -> Log.e(TAG, "Internet connection error. Try next time.")
            is IllegalStateException -> Log.e(TAG, "Wrong arguments error.")
            else -> Log.e(TAG, "Download images error.")
        }
    }

    private suspend fun loadConfig(): String = withContext(Dispatchers.IO) {
        runCatching {
            moviesApi.getConfiguration().images.let {
                it?.secureBaseUrl.toString()
            }
        }.onFailure {
            handleCallError(it)
        }.getOrNull() ?: ""
    }

    private suspend fun loadMovies(numberPage: Int): MoviesTmdb? {
        val moviesTMDB = withContext(Dispatchers.IO) {
            runCatching {
                moviesApi.getMovies(numberPage = numberPage)
            }.onFailure {
                handleCallError(it)
            }
        }
        return moviesTMDB.getOrNull()
    }

    private suspend fun loadMovie(id: Int): MovieDetailsTmdb? {
        val movieTmdb = withContext(Dispatchers.IO) {
            runCatching {
                moviesApi.getMovie(idMovie = id)
            }.onFailure {
                handleCallError(it)
            }
        }
        return movieTmdb.getOrNull()
    }

    suspend fun getMovies(numberPage: Int): List<Movies.Movie> =
        withContext(Dispatchers.IO) {
            val result = loadMovies(numberPage)?.let { it ->
                parseMovies(it)
            }
            result ?: emptyList()
        }

    suspend fun getMovie(id: Int): Movies.Movie? =
        withContext(Dispatchers.IO) {
            loadMovie(id)?.let { it ->
                parseMovieDetails(it)
            }
        }

    private suspend fun loadGenre(): GenreTmdb? {
        val genre = withContext(Dispatchers.IO) {
            runCatching {
                moviesApi.getGenre()
            }.onFailure {
                handleCallError(it)
            }
        }
        return genre.getOrNull()
    }

    private suspend fun loadActors(id: Int): List<Actor>? =
        withContext(Dispatchers.IO) {
            runCatching {
                val actors = moviesApi.getActors(id_movie = id)
                val baseImageUrl = loadConfig()
                parserActors(actors, baseImageUrl)
            }.onFailure {
                handleCallError(it)
            }.getOrNull()
        }

    private suspend fun parseMovies(data: MoviesTmdb): List<Movies.Movie> {
        val genre = loadGenre()?.genres ?: listOf()
        val genres = genre.associateBy { it.id }
        val baseImageUrl = loadConfig()
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
                listOfGenre = it.genreIds!!.map { key ->
                    genres[key]!!
                },
                listOfActors = listOf()
            )
        }
    }

    private suspend fun parseMovieDetails(data: MovieDetailsTmdb): Movies.Movie {
        val baseImageUrl = loadConfig()
        val actors = loadActors(data.id)
        return Movies.Movie(
            id = data.id,
            nameMovie = data.originalTitle,
            description = data.overview ?: "",
            poster = data.posterPath.let { poster -> "${baseImageUrl}w342${poster}" },
            detailPoster = data.backdropPath.let { poster -> "${baseImageUrl}w500${poster}" },
            rating = (data.voteAverage ?: 0.0F).toFloat(),
            reviews = data.voteCount ?: 0,
            rated = if (data.adult == true) "+16" else "+13",
            likes = false,
            listOfGenre = data.genres,
            duration = data.runtime ?: 0,
            listOfActors = actors ?: listOf()
        )
    }

    private fun parserActors(actors: MovieActorsTmdb, baseImageUrl: String): List<Actor> {
        return actors.cast.map {
            Actor(
                id = it.id,
                nameActor = it.originalName,
                photoActor = it.profilePath.let { poster -> "${baseImageUrl}w185${poster}" }
            )
        }
    }

    companion object {
        private const val TAG = "MOVIE_NETWORK"
    }
}