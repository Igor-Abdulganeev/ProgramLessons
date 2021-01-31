package ru.gorinih.androidacademy.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesTmdb(
    @SerialName("dates")
    val dates: Dates? = null,
    @SerialName("page")
    val page: Int? = null,
    @SerialName("total_pages")
    val totalPages: Int? = null,
    @SerialName("results")
    val results: List<ResultsItem> = listOf(),
    @SerialName("total_results")
    val totalResults: Int? = null
)

@Serializable
data class Dates(
    @SerialName("maximum")
    val maximum: String? = null,
    @SerialName("minimum")
    val minimum: String? = null
)

@Serializable
data class ResultsItem(
    @SerialName("overview")
    val overview: String? = "",
    @SerialName("original_language")
    val originalLanguage: String? = null,
    @SerialName("original_title")
    val originalTitle: String = "",
    @SerialName("video")
    val video: Boolean? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int?>? = listOf(),
    @SerialName("poster_path")
    val posterPath: String? = "",
    @SerialName("backdrop_path")
    val backdropPath: String? = "",
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("popularity")
    val popularity: Double? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = 0.0,
    @SerialName("id")
    val id: Int = 0,
    @SerialName("adult")
    val adult: Boolean? = false,
    @SerialName("vote_count")
    val voteCount: Int? = 0
)

@Serializable
data class GenreTmdb(
    @SerialName("genres")
    val genres: List<Genre>
)

