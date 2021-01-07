package ru.gorinih.androidacademy.data.repository

import ru.gorinih.androidacademy.data.loadMovieById
import ru.gorinih.androidacademy.data.model.Movies
import ru.gorinih.androidacademy.data.loadMovies

class MovieNetwork {
    suspend fun getMoviesNet(numberPage: Int): List<Movies.Movie> = loadMovies(numberPage)

    suspend fun getMovieDetailsNet(id: Int): Movies.Movie = loadMovieById(id)
}