package ru.gorinih.androidacademy.data.model

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
    data class Movie(
        val id: Int = 0,
        val nameMovie: String = "",
        val duration: Int = 0,
        val reviews: Int = 0,
        val rating: Float = 0.0F,
        val listOfGenre: List<Genre> = listOf(),
        val rated: String = "",
        val like: Boolean = false,
        val detailPoster: String = "",
        val poster: String = "",
        val description: String = "",
        val listOfActors: List<Actor> = listOf()
    ) : Movies()

    object Header : Movies()
}

/**
 * Модель актеры
 * @property @id Идентификатор
 * @property @nameActor Имя актера
 * @property @photoActor Ссылка на фото актера
 */
data class Actor(
    val id: Int = 0,
    val nameActor: String = "",
    val photoActor: String = ""
)

/**
 * Модель жанров
 * @property @id Идентификатор
 * @property @nameGenre Наименование жанра
 */
@Serializable
data class Genre(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val nameGenre: String = ""
)
