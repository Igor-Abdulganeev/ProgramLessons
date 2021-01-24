package ru.gorinih.androidacademy.data.repository

import ru.gorinih.androidacademy.data.db.MoviesDao
import ru.gorinih.androidacademy.data.model.Movies
import ru.gorinih.androidacademy.data.model.RelationActorsOfMovie
import ru.gorinih.androidacademy.data.model.RelationGenresOfMovie
import ru.gorinih.androidacademy.data.model.TmpIdMovies
import ru.gorinih.androidacademy.data.network.MovieNetwork

class MoviesRepository(private val movieDao: MoviesDao) {

    private val dataNet = MovieNetwork()

    private suspend fun insertIdMovies(ids: List<TmpIdMovies>) {
        movieDao.insertIdMovie(ids)
    }

    private suspend fun deleteIdMovies() = movieDao.deleteIdMovie()

    suspend fun getMovieFromNetwork(id: Int): Movies.Movie? = dataNet.getMovieDetailsNet(id = id)

    suspend fun insertUpdateMovies(items: List<Movies.Movie>) {
        movieDao.insertUpdateMovies(items = items)
        items.forEach {
            val singleMovie = dataNet.getMovieDetailsNet(it.id)
            if (singleMovie != null) {
                val movieGenres = singleMovie.listOfGenre.map { itGenre ->
                    RelationGenresOfMovie(
                        id = 0,
                        movie_id = singleMovie.id,
                        genre_id = itGenre.id
                    )
                }
                val movieActors = singleMovie.listOfActors.map { itActor ->
                    RelationActorsOfMovie(
                        id = 0,
                        movie_id = singleMovie.id,
                        actor_id = itActor.id
                    )
                }
                //TODO запараллелить
                movieDao.insertGenres(singleMovie.listOfGenre)
                movieDao.insertActors(singleMovie.listOfActors)
                movieDao.insertGenresOfMovie(movieGenres)
                movieDao.insertActorsOfMovie(movieActors)
            }
        }
    }

    suspend fun getMovies(numberPage: Int, listIdMovies: List<Int>): List<Movies.Movie> {
        return if (numberPage == 1) {
            val resultDb = getMoviesFromDatabase(listIdMovies = listIdMovies)
            if (resultDb.isEmpty()) getMoviesFromNetwork(numberPage = numberPage) else
                return resultDb
        } else {
            val resultNet = getMoviesFromNetwork(numberPage = numberPage)
            if (resultNet.isEmpty()) getMoviesFromDatabase(listIdMovies = listIdMovies)
            else
                return resultNet
        }
    }

    private suspend fun getMoviesFromDatabase(
        listIdMovies: List<Int>
    ): List<Movies.Movie> {
        val list: List<Movies.Movie>
        if (listIdMovies.count() > 0) {
            val idMovies: List<TmpIdMovies> = listIdMovies.map { TmpIdMovies(0, it) }
            insertIdMovies(idMovies)
            list = movieDao.loadMoviesFromDB()
            if (idMovies.count() > 0) deleteIdMovies()
        } else
            list = movieDao.loadMoviesFromDBFirst()
        if (list.isNotEmpty()) {
            list.map { it ->
                it.listOfGenre = movieDao.loadGenresById(it.id) ?: emptyList()
            }
        }
        return list
    }

    private suspend fun getMoviesFromNetwork(numberPage: Int): List<Movies.Movie> =
        dataNet.getMoviesNet(numberPage = numberPage)

    suspend fun getMovie(idMove: Int): Movies.Movie? {
        val movie = movieDao.loadMovieFromDB(idMovie = idMove)
        return if (movie != null) {
            movie.listOfGenre = movieDao.loadGenresById(idMove) ?: emptyList()
            movie.listOfActors = movieDao.loadActorsById(idMove) ?: emptyList()
            movie
        } else
            getMovieFromNetwork(id = idMove)
    }
}