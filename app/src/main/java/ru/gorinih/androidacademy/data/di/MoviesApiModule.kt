package ru.gorinih.androidacademy.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.gorinih.androidacademy.data.db.MoviesDatabase
import ru.gorinih.androidacademy.data.network.MoviesApi

@Module
@ExperimentalSerializationApi
class MoviesApiModule {

    @Provides
    fun newInstanceApi(): MoviesApi {
        val logging = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
        val json = Json {
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(logging)
            .build()
            .create(MoviesApi::class.java)
    }
}