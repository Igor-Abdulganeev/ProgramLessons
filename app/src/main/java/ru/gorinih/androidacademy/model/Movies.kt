package ru.gorinih.androidacademy.model

sealed class Movies {
    data class Movie(
        val id: Int,    //1 - val id: Int, Идентификатор фильма
        val nameMovie: String,  //2 - val title: String, Наименование фильма
        val duration: Int, //9 - val runtime: Int, Продолжительность фильма
        val reviews: Int,   //7 - val numberOfRatings: Int, Количество отзывов
        val rating: Float,  //6 - val ratings: Float, Рейтинг в звездочках
        val listOfGenre: List<Genre>, // String, //10 - val genres: List<Genre>, Жанр
        val rated: String,  //8 - val minimumAge: Int, Возрастной рейтинг
        val like: Boolean,
        val detailPoster: String,   //5 - val backdrop: String, Постер детализации фильма
        val poster: String, //4 - val poster: String, Постер фильма
        val description: String,    //3 - val overview: String, Краткое описание фильма
        val listOfActors: List<Actor>   //11 - val actors: List<ru.gorinih.androidacademy.data.Actor>
    ) : Movies()

    object Header : Movies()
}

data class Actor(
    val id: Int,    //val id: Int, Идентификатор
//    val idMovie: Int,
    val nameActor: String,  // val name: String, Имя актера
    val photoActor: String  //val picture: String  Ссылка на фото актера
)

data class Genre(
    val id: Int, // Идентификатор
    val nameGenre: String // Наименование жанра
)

