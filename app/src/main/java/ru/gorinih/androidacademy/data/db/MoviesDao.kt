package ru.gorinih.androidacademy.data.db

import androidx.room.*
import ru.gorinih.androidacademy.data.models.*

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateMovies(items: List<Movies.Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActors(items: List<Actor>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(items: List<Genre>)

    @Insert
    suspend fun insertActorsOfMovie(items: List<RelationActorsOfMovie>)

    @Insert
    suspend fun insertGenresOfMovie(items: List<RelationGenresOfMovie>)

    @Query(
        "SELECT * FROM movies WHERE id NOT IN (SELECT id_movie FROM tmp_id_movies) LIMIT 20"
    )
    suspend fun loadMoviesFromDB(): List<Movies.Movie>

    @Query(
        "SELECT * FROM movies LIMIT 20"
    )
    suspend fun loadMoviesFromDBFirst(): List<Movies.Movie>

    @Query(
        "SELECT * FROM genres WHERE id IN (SELECT id_genre FROM relation_genres_of_movie WHERE id_movie = :id_movie)"
    )
    suspend fun loadGenresById(id_movie: Int): List<Genre>?

    @Query(
        "SELECT * FROM actors WHERE id IN (SELECT id_actor FROM relation_actors_of_movie WHERE id_movie = :id_movie)"
    )
    suspend fun loadActorsById(id_movie: Int): List<Actor>?

    @Query(
        "SELECT * FROM MOVIES WHERE id = :idMovie LIMIT 1"
    )
    suspend fun loadMovieFromDB(idMovie: Int): Movies.Movie?

    //--------tmp-------//

    @Insert
    suspend fun insertIdMovie(id_movies: List<TmpIdMovies>)

    @Query("DELETE FROM tmp_id_movies")
    suspend fun deleteIdMovie()
}
