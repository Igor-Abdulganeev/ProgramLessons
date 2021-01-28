package ru.gorinih.androidacademy.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Модель фильма
 * @property @id Идентификатор фильма
 * @property @nameMovie Наименование фильма
 * @property @duration Продолжительность фильма
 * @property @reviews Количество отзывов
 * @property @rating Рейтинг в звездочках
 * @property @listOfGenre Список жанров
 * @property @rated Возрастной рейтинг
 * @property @like Избранное
 * @property @detailPoster Постер детализации фильма
 * @property @poster Постер фильма
 * @property @description Краткое описание фильма
 * @property @listOfActors Список актеров
 */
sealed class Movies {
    /*
        data class Movie(
            val id: Int = 0,
            val nameMovie: String = "",
            val duration: Int = 0,
            val reviews: Int = 0,
            val rating: Float = 0.0F,
            val listOfGenre: List<Genre> = emptyList(),
            val rated: String = "",
            val like: Boolean = false,
            val detailPoster: String = "",
            val poster: String = "",
            val description: String = "",
            val listOfActors: List<Actor> = listOf()
        ) : Movies()
    */
    @Entity(tableName = "movies")
    data class Movie(
        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: Int = 0,
        @ColumnInfo(name = "name_movie")
        var nameMovie: String = "",
        @ColumnInfo(name = "duration")
        var duration: Int = 0,
        @ColumnInfo(name = "count_reviews")
        var reviews: Int = 0,
        @ColumnInfo(name = "stars_rating")
        var rating: Float = 0.0F,
        @Ignore
        var listOfGenre: List<Genre> = emptyList(),
        @ColumnInfo(name = "adult_rating")
        var rated: String = "",
        @ColumnInfo(name = "likes")
        var likes: Boolean = false,
        @ColumnInfo(name = "detail_poster_url")
        var detailPoster: String = "",
        @ColumnInfo(name = "main_poster_url")
        var poster: String = "",
        @ColumnInfo(name = "description")
        var description: String = "",
        @Ignore
        var listOfActors: List<Actor> = listOf()
    ) : Movies()

    object Header : Movies()
}

/**
 * Модель актеры
 * @property @id Идентификатор
 * @property @nameActor Имя актера
 * @property @photoActor Ссылка на фото актера
 */
@Entity(tableName = "actors")
data class Actor(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "name_actor")
    val nameActor: String = "",
    @ColumnInfo(name = "photo_actor_url")
    val photoActor: String = ""

)

@Entity(tableName = "relation_actors_of_movie")
data class RelationActorsOfMovie(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "id_movie")
    val movie_id: Int,
    @ColumnInfo(name = "id_actor")
    val actor_id: Int
)

/**
 * Модель жанров
 * @property @id Идентификатор
 * @property @nameGenre Наименование жанра
 */
@Serializable
@Entity(tableName = "genres")
data class Genre(
    @SerialName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @SerialName("name")
    @ColumnInfo(name = "name_genre")
    val nameGenre: String = ""
)

@Entity(tableName = "relation_genres_of_movie")
data class RelationGenresOfMovie(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "id_movie")
    val movie_id: Int,
    @ColumnInfo(name = "id_genre")
    val genre_id: Int
)

@Entity(tableName = "tmp_id_movies")
data class TmpIdMovies(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "id_movie")
    val idMovie: Int
)