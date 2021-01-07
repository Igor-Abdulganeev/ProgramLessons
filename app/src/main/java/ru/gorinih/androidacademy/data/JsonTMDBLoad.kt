package ru.gorinih.androidacademy.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.gorinih.androidacademy.data.model.*
import java.io.IOException

@Suppress("unused")
internal suspend fun loadMovies(numberPage: Int): List<Movies.Movie> =
    withContext(Dispatchers.IO) {
        val result = loadTmdbMovies(numberPage)?.let { it ->
            parseMovies(it)
        }
        result ?: listOf(Movies.Movie())
    }


@Suppress("unused")
internal suspend fun loadMovieById(id: Int): Movies.Movie =
    withContext(Dispatchers.IO) {
        loadTmdbMovie(id)?.let { it ->
            parseMovieDetails(it)
        } ?: Movies.Movie()
    }

@ExperimentalSerializationApi
private suspend fun loadTmdbMovie(id: Int): MovieDetailsTmdb? {
    val movieTmdb = withContext(Dispatchers.IO) {
        runCatching {
            RetrofitModule.MOVIES_API.getMovieById(idMovie = id)
        }.onFailure {
            handleCallError(it)
        }
    }
    return movieTmdb.getOrNull()
}

@ExperimentalSerializationApi
private suspend fun loadTmdbMovies(numberPage: Int): MoviesTmdb? {
    val moviesTMDB = withContext(Dispatchers.IO) {
        runCatching {
            RetrofitModule.MOVIES_API.getMoviesFromTmdb(numberPage = numberPage)
        }.onFailure {
            handleCallError(it)
        }
    }
    return moviesTMDB.getOrNull()
}

@ExperimentalSerializationApi
private suspend fun loadGenre(): GenreTmdb? {
    val genre = withContext(Dispatchers.IO) {
        kotlin.runCatching {
            RetrofitModule.MOVIES_API.getGenre()
        }.onFailure {
            handleCallError(it)
        }
    }
    return genre.getOrNull()
}

fun handleCallError(throwable: Throwable) {
    when (throwable) {
        is IOException -> Log.e(TAG, "Internet connection error. Try next time.")
        is IllegalStateException -> Log.e(TAG, "Wrong arguments error.")
        else -> Log.e(TAG, "Download images error.")
    }
}

private interface APITmdbNowPlaying {
    @GET("movie/now_playing?api_key=69f47bb575e0708f5804d2b046fcd103")
    suspend fun getMoviesFromTmdb(@Query("page") numberPage: Int): MoviesTmdb

    @GET("genre/movie/list?api_key=69f47bb575e0708f5804d2b046fcd103")
    suspend fun getGenre(): GenreTmdb

    @GET("movie/{id_movie}?api_key=69f47bb575e0708f5804d2b046fcd103")
    suspend fun getMovieById(@Path("id_movie") idMovie: Int): MovieDetailsTmdb

    @GET("configuration?api_key=69f47bb575e0708f5804d2b046fcd103")
    suspend fun getConfiguration(): ConfigurationTmdb

    @GET("movie/{id_movie}/credits?api_key=69f47bb575e0708f5804d2b046fcd103")
    suspend fun getActors(@Path("id_movie") id_movie: Int): MovieActorsTmdb
}

@ExperimentalSerializationApi
private object RetrofitModule {
    private val logging = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
    private val json = Json {
        ignoreUnknownKeys = true
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(logging)
        .build()

    val MOVIES_API: APITmdbNowPlaying = retrofit.create(APITmdbNowPlaying::class.java)
}

@ExperimentalSerializationApi
private suspend fun loadConfig(): String = withContext(Dispatchers.IO) {
    runCatching {
        RetrofitModule.MOVIES_API.getConfiguration().images.let {
            it?.secureBaseUrl.toString()
        }
    }.onFailure {
        handleCallError(it)
    }.getOrNull() ?: ""
}

@ExperimentalSerializationApi
private suspend fun loadActors(id: Int): List<Actor>? =
    withContext(Dispatchers.IO) {
        runCatching {
            val actors = RetrofitModule.MOVIES_API.getActors(id_movie = id)
            parserActors(actors)
        }.onFailure {
            handleCallError(it)
        }.getOrNull()
    }

private suspend fun parserActors(actors: MovieActorsTmdb): List<Actor> {
    val baseImageUrl = loadConfig()
    return actors.cast.map {
        Actor(
            id = it.id,
            nameActor = it.originalName,
            photoActor = it.profilePath.let { poster -> "${baseImageUrl}w185${poster}" }
        )
    }
}

@ExperimentalSerializationApi
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
            like = false,
            listOfGenre = it.genreIds!!.map { key ->
                genres[key]!!
            },
            listOfActors = listOf()
        )
    }
}

@ExperimentalSerializationApi
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
        like = false,
        listOfGenre = data.genres,
        duration = data.runtime ?: 0,
        listOfActors = actors ?: listOf()
    )
}

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val TAG = "LOAD_JSON_FROM_TMDB"
