package ru.gorinih.androidacademy.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsTmdb(
    @SerialName("original_language")
    val originalLanguage: String? = null,
    @SerialName("imdb_id")
    val imdbId: String? = null,
    @SerialName("video")
    val video: Boolean? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = "",
    @SerialName("revenue")
    val revenue: Int? = null,
    @SerialName("genres")
    val genres: List<Genre>,
    @SerialName("popularity")
    val popularity: Double? = null,
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountriesItem?>? = null,
    @SerialName("id")
    val id: Int = 0,
    @SerialName("vote_count")
    val voteCount: Int? = 0,
    @SerialName("budget")
    val budget: Int? = null,
    @SerialName("overview")
    val overview: String? = "",
    @SerialName("original_title")
    val originalTitle: String = "",
    @SerialName("runtime")
    val runtime: Int? = 0,
    @SerialName("poster_path")
    val posterPath: String? = "",
    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguagesItem?>? = null,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompaniesItem?>? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = 0.0,
    @SerialName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection? = null,
    @SerialName("tagline")
    val tagline: String? = null,
    @SerialName("adult")
    val adult: Boolean? = false,
    @SerialName("homepage")
    val homepage: String? = null,
    @SerialName("status")
    val status: String? = null
)

@Serializable
data class ProductionCountriesItem(
    @SerialName("iso_3166_1")
    val iso31661: String? = null,
    @SerialName("name")
    val name: String? = null
)

@Serializable
data class SpokenLanguagesItem(
    @SerialName("name")
    val name: String? = null,
    @SerialName("iso_639_1")
    val iso6391: String? = null,
    @SerialName("english_name")
    val englishName: String? = null
)

@Serializable
data class BelongsToCollection(
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("poster_path")
    val posterPath: String? = null
)

@Serializable
data class ProductionCompaniesItem(
    @SerialName("logo_path")
    val logoPath: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("origin_country")
    val originCountry: String? = null
)
