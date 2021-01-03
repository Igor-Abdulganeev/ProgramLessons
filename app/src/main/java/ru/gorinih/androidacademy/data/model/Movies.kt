package ru.gorinih.androidacademy.data

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
        val id: Int,
        val nameMovie: String,
        val duration: Int,
        val reviews: Int,
        val rating: Float,
        val listOfGenre: List<Genre>,
        val rated: String,
        val like: Boolean,
        val detailPoster: String,
        val poster: String,
        val description: String,
        val listOfActors: List<Actor>
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
    val id: Int,
    val nameActor: String,
    val photoActor: String
)

/**
 * Модель жанров
 * @property @id Идентификатор
 * @property @nameGenre Наименование жанра
 */
data class Genre(
    val id: Int,
    val nameGenre: String
)
