package ru.gorinih.androidacademy.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.gorinih.androidacademy.data.models.*

interface MoviesApi {
    @GET("configuration?api_key=69f47bb575e0708f5804d2b046fcd103")
    suspend fun getConfiguration(): ConfigurationTmdb

    @GET("movie/now_playing?api_key=69f47bb575e0708f5804d2b046fcd103&language=ru")
    suspend fun getMovies(@Query("page") numberPage: Int): MoviesTmdb

    @GET("movie/{id_movie}?api_key=69f47bb575e0708f5804d2b046fcd103&language=ru")
    suspend fun getMovie(@Path("id_movie") idMovie: Int): MovieDetailsTmdb

    @GET("genre/movie/list?api_key=69f47bb575e0708f5804d2b046fcd103&language=ru")
    suspend fun getGenre(): GenreTmdb

    @GET("movie/{id_movie}/credits?api_key=69f47bb575e0708f5804d2b046fcd103&language=ru")
    suspend fun getActors(@Path("id_movie") id_movie: Int): MovieActorsTmdb

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        @ExperimentalSerializationApi
        fun newInstance(): MoviesApi {
            val logging = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
            val json = Json {
                ignoreUnknownKeys = true
            }
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .client(logging)
                .build()
                .create(MoviesApi::class.java)
        }
    }
}