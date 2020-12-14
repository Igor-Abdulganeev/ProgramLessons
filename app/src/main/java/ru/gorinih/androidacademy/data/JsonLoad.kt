package ru.gorinih.androidacademy.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.gorinih.androidacademy.model.Actor
import ru.gorinih.androidacademy.model.Genre
import ru.gorinih.androidacademy.model.Movies


private val jsonFormat = Json { ignoreUnknownKeys = true }

@Serializable
private class JsonGenre(val id: Int, val name: String)

@Serializable
private class JsonActor(
    val id: Int,
    val name: String,
    @SerialName("profile_path")
    val profilePicture: String
)

@Serializable
private class JsonMovie(
    val id: Int,
    val title: String,
    @SerialName("poster_path")
    val posterPicture: String,
    @SerialName("backdrop_path")
    val backdropPicture: String,
    val runtime: Int,
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    val actors: List<Int>,
    @SerialName("vote_average")
    val ratings: Float,
    @SerialName("vote_count")
    val votesCount: Int,
    val overview: String,
    val adult: Boolean
)

private suspend fun loadGenres(context: Context): List<Genre> = withContext(Dispatchers.IO) {
    val data = readAssetFileToString(context, "genres.json")
    parseGenres(data)
}

internal fun parseGenres(data: String): List<Genre> {
    val jsonGenres = jsonFormat.decodeFromString<List<JsonGenre>>(data)
    return jsonGenres.map { Genre(id = it.id, nameGenre = it.name) }
}

private fun readAssetFileToString(context: Context, fileName: String): String {
    val stream = context.assets.open(fileName)
    return stream.bufferedReader().readText()
}

private suspend fun loadActors(context: Context): List<Actor> = withContext(Dispatchers.IO) {
    val data = readAssetFileToString(context, "people.json")
    parseActors(data)
}

internal fun parseActors(data: String): List<Actor> {
    val jsonActors = jsonFormat.decodeFromString<List<JsonActor>>(data)
    return jsonActors.map { Actor(id = it.id, nameActor = it.name, photoActor = it.profilePicture) }
}

@Suppress("unused")
internal suspend fun loadMovies(context: Context): List<Movies.Movie> =
    withContext(Dispatchers.IO) {
        val genresMap = loadGenres(context)
        val actorsMap = loadActors(context)

        val data = readAssetFileToString(context, "data.json")
        parseMovies(data, genresMap, actorsMap)
    }

internal fun parseMovies(
    data: String,
    genres: List<Genre>,
    actors: List<Actor>
): List<Movies.Movie> {
    val genresMap = genres.associateBy { it.id }
    val actorsMap = actors.associateBy { it.id }

    val jsonMovies = jsonFormat.decodeFromString<List<JsonMovie>>(data)

    return jsonMovies.map { jsonMovie ->
        @Suppress("unused")
        Movies.Movie(
            id = jsonMovie.id,
            nameMovie = jsonMovie.title,
            description = jsonMovie.overview,
            poster = jsonMovie.posterPicture,
            detailPoster = jsonMovie.backdropPicture,
            rating = jsonMovie.ratings,
            reviews = jsonMovie.votesCount,
            rated = if (jsonMovie.adult) "+16" else "+13",
            duration = jsonMovie.runtime,
            like = false,
            listOfGenre = jsonMovie.genreIds.map {
                genresMap[it] ?: throw IllegalArgumentException("Genre not found")
            },
            listOfActors = jsonMovie.actors.map {
                actorsMap[it] ?: throw IllegalArgumentException("Actor not found")
            }
        )
    }
}