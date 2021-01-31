package ru.gorinih.androidacademy.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.data.MoviesPagingSource
import ru.gorinih.androidacademy.data.db.MoviesDao
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.data.models.RelationActorsOfMovie
import ru.gorinih.androidacademy.data.models.RelationGenresOfMovie
import ru.gorinih.androidacademy.data.models.TmpIdMovies
import ru.gorinih.androidacademy.data.network.MoviesNetwork

@ExperimentalSerializationApi
class MoviesRepository(private val movieDao: MoviesDao, private val movieNetwork: MoviesNetwork) {

    private suspend fun insertIdMovies(ids: List<TmpIdMovies>) {
        movieDao.insertIdMovie(ids)
    }

    private suspend fun deleteIdMovies() = movieDao.deleteIdMovie()

    suspend fun getMovieFromNetwork(id: Int): Movies.Movie? = movieNetwork.getMovie(id = id)

    suspend fun insertUpdateMovies(items: List<Movies.Movie>) {
        movieDao.insertUpdateMovies(items = items)
        items.forEach {
            val singleMovie = movieNetwork.getMovie(it.id)
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
                coroutineScope {
                    launch { movieDao.insertGenres(singleMovie.listOfGenre) }
                    launch { movieDao.insertActors(singleMovie.listOfActors) }
                    launch { movieDao.insertGenresOfMovie(movieGenres) }
                    launch { movieDao.insertActorsOfMovie(movieActors) }
                }

            }
        }
    }

    suspend fun getMovies(numberPage: Int, listIdMovies: List<Int>): List<Movies.Movie> {
        return if (numberPage == 1) {
            val resultDb = getMoviesFromDatabase(listIdMovies = listIdMovies)
            if (resultDb.isEmpty()) getMoviesFromNetwork(numberPage = numberPage) else
                if (resultDb.count() == 0) getMoviesFromNetwork(numberPage = numberPage) else
                    return resultDb
        } else {
            val resultNet = getMoviesFromNetwork(numberPage = numberPage)
            if (resultNet.isEmpty()) getMoviesFromDatabase(listIdMovies = listIdMovies)
            else
                return resultNet
        }
    }

    @InternalCoroutinesApi
    fun getPageMovie(): Flow<PagingData<Movies>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviesPagingSource.newInstance(movieNetwork) }
        ).flow
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
/*
// Flow method (don`t work)
        if (list.count()>0){
            list.flatMapMerge {
               flow {
                   emit (it.forEach { itMovie ->
                   itMovie.listOfGenre = movieDao.loadGenresById(itMovie.id) ?: emptyList()
               })
               }
            }

        }
*/
/*
// LiveData method
        if (list.value?.isNotEmpty() == true) {
            list.value?.map {
                it.listOfGenre = movieDao.loadGenresById(it.id) ?: emptyList()
            }
        }
*/
// Simple value method
        if (list.isNotEmpty()) {
            list.map { it ->
                it.listOfGenre = movieDao.loadGenresById(it.id) ?: emptyList()
            }
        }
        return list
    }

    private suspend fun getMoviesFromNetwork(numberPage: Int): List<Movies.Movie> =
        movieNetwork.getMovies(numberPage = numberPage)

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