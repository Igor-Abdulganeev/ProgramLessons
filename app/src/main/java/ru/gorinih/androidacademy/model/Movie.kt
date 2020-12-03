package ru.gorinih.androidacademy.model

data class Movie(
    val id: Int,
    val nameMovie: String,
    val movieDuration: Int,
    val reviews: Int,
    val rating: Int,
    val movieGenre: String,
    val rated: String,
    val like: Boolean,
    val poster: String
)



