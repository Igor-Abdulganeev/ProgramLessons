package ru.gorinih.androidacademy.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okio.IOException
import ru.gorinih.androidacademy.data.models.*

class MovieNetwork(private val moviesApi: MoviesApi) {

    suspend fun loadMovie(id: Int): Movies.Movie? {
        var movieDetailsTmdb: MovieDetailsTmdb? = null
        var configurationTmdb: ConfigurationTmdb? = null
        var actorsTmdb: MovieActorsTmdb? = null
        coroutineScope {
            runCatching {
                movieDetailsTmdb = withContext(Dispatchers.IO) { loadMovieDetails(id = id) }
                configurationTmdb = withContext(Dispatchers.IO) { loadConfiguration() }
                actorsTmdb = withContext(Dispatchers.IO) { loadActors(id = id) }
            }.onFailure { handleCallError(it) }
        }
        val baseImageUrl = configurationTmdb?.images?.secureBaseUrl ?: "http://image.tmdb.org/t/p/"
        val actors = parserActors(actorsTmdb, baseImageUrl)
        return parseMovieDetails(movieDetailsTmdb, actors, baseImageUrl)
    }

    private suspend fun loadMovieDetails(id: Int): MovieDetailsTmdb? = withContext(Dispatchers.IO) {
        moviesApi.getMovie(idMovie = id)
    }

    private suspend fun loadConfiguration(): ConfigurationTmdb? = withContext(Dispatchers.IO) {
        moviesApi.getConfiguration()
    }

    private suspend fun loadActors(id: Int): MovieActorsTmdb? = withContext(Dispatchers.IO) {
        moviesApi.getActors(idMovie = id)
    }

    private fun parseMovieDetails(
        data: MovieDetailsTmdb?,
        actors: List<Actor>,
        baseImageUrl: String
    ): Movies.Movie? {
        return data?.let {
            Movies.Movie(
                id = it.id,
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
                listOfActors = actors
            )
        }
    }

    private fun parserActors(actors: MovieActorsTmdb?, baseImageUrl: String): List<Actor> {
        return actors?.cast?.map {
            Actor(
                id = it.id,
                nameActor = it.originalName,
                photoActor = it.profilePath.let { poster -> "${baseImageUrl}w185${poster}" }
            )
        } ?: emptyList()
    }

    private fun handleCallError(throwable: Throwable) {
        when (throwable) {
            is IOException -> Log.e(TAG, "Internet connection error. Try next time.", throwable)
            is IllegalStateException -> Log.e(TAG, "Wrong arguments error.", throwable)
            else -> "Another error, please send message about it"
        }
    }

    companion object {
        private const val TAG = "MovieNetwork"
    }
}