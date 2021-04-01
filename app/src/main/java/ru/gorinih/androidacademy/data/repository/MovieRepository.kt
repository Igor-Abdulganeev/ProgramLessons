package ru.gorinih.androidacademy.data.repository

import android.util.Log
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okio.IOException
import ru.gorinih.androidacademy.data.db.MoviesDatabase
import ru.gorinih.androidacademy.data.models.*
import ru.gorinih.androidacademy.data.network.MoviesApi
import ru.gorinih.androidacademy.di.FragmentScope
import javax.inject.Inject
import javax.inject.Singleton

@FragmentScope
class MovieRepository @Inject constructor(
    private val moviesApi: MoviesApi,
    private val moviesDatabase: MoviesDatabase
) {
    init {
        Log.d("ViewModel", "Movie Repository init")
    }

    @ExperimentalStdlibApi
    suspend fun loadMovie(id: Int): Movies.Movie? {
        val result: Movies.Movie? = moviesDatabase.withTransaction {
            val response = moviesDatabase.moviesDao.loadMovieFromDB(id)
            response?.let {
                withContext(Dispatchers.IO) { response.listOfGenre = loadGenres(id) }
                withContext(Dispatchers.IO) { response.listOfActors = loadActors(id) }
            }
            response
        }
        result?.also { return it }
        return loadMovieNetwork(id)
    }

    suspend fun loadMovieNetwork(id: Int): Movies.Movie? {
        return withContext(Dispatchers.IO) {
            var movieDetailsTmdb: MovieDetailsTmdb? = null
            var configurationTmdb: ConfigurationTmdb? = null
            var actorsTmdb: MovieActorsTmdb? = null

            coroutineScope {
                runCatching<MovieRepository, Unit> {
                    movieDetailsTmdb = withContext(Dispatchers.IO) { getMovieDetails(id = id) }
                    configurationTmdb =
                        withContext<ConfigurationTmdb?>(Dispatchers.IO) { getConfiguration() }
                    actorsTmdb = withContext(Dispatchers.IO) { getActors(id = id) }
                }.onFailure { handleCallError(it) }
            }
            val baseImageUrl =
                configurationTmdb?.images?.secureBaseUrl ?: "http://image.tmdb.org/t/p/"
            val actors = parserActors(actorsTmdb, baseImageUrl)
            val response = parseMovieDetails(movieDetailsTmdb, actors, baseImageUrl)

            val movieActors = actors.map {
                RelationActorsOfMovie(
                    id = 0,
                    movie_id = id,
                    actor_id = it.id
                )
            }
            response?.let {
                coroutineScope {
                    withContext<Unit>(Dispatchers.IO) {
                        moviesDatabase.moviesDao.insertActors(
                            actors
                        )
                    }
                    withContext<Unit>(Dispatchers.IO) {
                        moviesDatabase.moviesDao.insertActorsOfMovie(
                            movieActors
                        )
                    }
                }
            }
            response
        }
    }

    private suspend fun loadGenres(idMovie: Int): List<Genre> = withContext(Dispatchers.IO) {
        moviesDatabase.moviesDao.loadGenresById(idMovie) ?: emptyList()
    }

    private suspend fun loadActors(idMovie: Int): List<Actor> = withContext(Dispatchers.IO) {
        moviesDatabase.moviesDao.loadActorsById(idMovie) ?: emptyList()
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

    private suspend fun getMovieDetails(id: Int): MovieDetailsTmdb? = withContext(Dispatchers.IO) {
        moviesApi.getMovie(idMovie = id)
    }

    private suspend fun getConfiguration(): ConfigurationTmdb? = withContext(Dispatchers.IO) {
        moviesApi.getConfiguration()
    }

    private suspend fun getActors(id: Int): MovieActorsTmdb? = withContext(Dispatchers.IO) {
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