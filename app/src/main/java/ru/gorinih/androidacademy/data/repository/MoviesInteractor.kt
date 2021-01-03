package ru.gorinih.androidacademy.data.repository

import ru.gorinih.androidacademy.data.Movies

class MoviesInteractor(private val data: MovieStorage) {

    suspend fun getMovies(): List<Movies.Movie> = data.getMovies()

    suspend fun getMoviesById(id: Int): Movies.Movie = data.getMoviesById(id)
}