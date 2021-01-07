package ru.gorinih.androidacademy.data.repository

import ru.gorinih.androidacademy.data.model.Movies

class MoviesInteractor(private val data: MovieStorage) {

    private val dataNet = MovieNetwork()

    suspend fun getMovies(): List<Movies.Movie> = data.getMovies()

    suspend fun getMoviesById(id: Int): Movies.Movie = data.getMoviesById(id = id)

    suspend fun getMoviesNet(numberPage: Int): List<Movies.Movie> =
        dataNet.getMoviesNet(numberPage = numberPage)

    suspend fun getMoviesByIdNet(id: Int): Movies.Movie = dataNet.getMovieDetailsNet(id = id)
}