package ru.gorinih.androidacademy.data.repository

import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.data.network.MovieNetwork

class MovieRepository(private val movieNetwork: MovieNetwork) {

    suspend fun loadMovie(id: Int): Movies.Movie? = movieNetwork.loadMovie(id = id)


}