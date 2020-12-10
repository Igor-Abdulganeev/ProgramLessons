package ru.gorinih.androidacademy.model

import ru.gorinih.androidacademy.model.Movies.Header
import ru.gorinih.androidacademy.model.Movies.Movie


sealed class Movies() {
    data class Movie(
        val id: Int,
        val nameMovie: String,
        val movieDuration: Int,
        val reviews: Int,
        val rating: Float,
        val movieGenre: String,
        val rated: String,
        val like: Boolean,
        val detailPoster: String,
        val poster: String,
        val description: String,
        val listOfActors: List<Actor>
    ) : Movies()

    object Header : Movies()
}

data class Actor(
    val id: Int,
    val idMovie: Int,
    val nameActor: String,
    val photoActor: String
)

private val listActors = listOf<Actor>(
    Actor(
        1,
        1,
        "Robert Downey Jr.",
        "http://lardis.ru/academ/webp/pic_actor_photo_robert_downey_jr.webp"
    ),
    Actor(
        2,
        1,
        "Antonio Banderas",
        "http://lardis.ru/academ/webp/pic_actor_photo_antonio_banderas.webp"
    ),
    Actor(
        3,
        1,
        "Michael Sheen",
        "http://lardis.ru/academ/webp/pic_actor_photo_michael_sheen.webp"
    ),

    Actor(
        4,
        1,
        "Jim Broadbent",
        "http://lardis.ru/academ/webp/pic_actor_photo_jim_broadbent.webp"
    ),
    Actor(
        5,
        1,
        "Jessie Buckley",
        "http://lardis.ru/academ/webp/pic_actor_photo_jessie_buckley.webp"
    ),
    Actor(
        6,
        1,
        "Harry Colett",
        "http://lardis.ru/academ/webp/pic_actor_photo_harry_colett.webp"
    ),
    Actor(
        7,
        2,
        "Kristen Stewart",
        "http://lardis.ru/academ/webp/pic_actor_photo_kristen_stewart.webp"
    ),
    Actor(
        8,
        2,
        "Vincent Cassel",
        "http://lardis.ru/academ/webp/pic_actor_photo_vincent_cassel.webp"
    ),
    Actor(
        9,
        2,
        "Mamoudou Athie",
        "http://lardis.ru/academ/webp/pic_actor_photo_mamoudou_athie.webp"
    ),
    Actor(
        10,
        2,
        "T. J. Miller",
        "http://lardis.ru/academ/webp/pic_actor_photo_tj_miller.webp"
    ),
    Actor(
        11,
        2,
        "John Gallagher Jr.",
        "http://lardis.ru/academ/webp/pic_actor_photo_john_gallagher_jr.webp"
    ),
    Actor(
        12,
        2,
        "Jessica Henwick",
        "http://lardis.ru/academ/webp/pic_actor_photo_jessica_henwick.webp"
    ),
    Actor(
        13,
        2,
        "Gunner Wright",
        "http://lardis.ru/academ/webp/pic_actor_photo_gunner_wright.webp"
    ),
    Actor(
        14,
        2,
        "Fiona Rene",
        "http://lardis.ru/academ/webp/pic_actor_photo_fiona_rene.webp"
    ),
    Actor(
        15,
        2,
        "Amanda Troop",
        "http://lardis.ru/academ/webp/pic_actor_photo_amanda_troop.webp"
    ),
    Actor(
        16,
        3,
        "Harrison Ford",
        "http://lardis.ru/academ/webp/pic_actor_photo_harrison_ford.webp"
    ),
    Actor(
        17,
        3,
        "Omar Sy",
        "http://lardis.ru/academ/webp/pic_actor_photo_omar_sy.webp"
    ),
    Actor(
        18,
        3,
        "Cara Gee",
        "http://lardis.ru/academ/webp/pic_actor_photo_cara_gee.webp"
    ),
    Actor(
        19,
        3,
        "Dan Stevens",
        "http://lardis.ru/academ/webp/pic_actor_photo_dan_stevens.webp"
    ),
    Actor(
        20,
        3,
        "Bradley Whitford",
        "http://lardis.ru/academ/webp/pic_actor_photo_bradley_whitford.webp"
    ),
    Actor(
        21,
        3,
        "Jean Louisa Kelly",
        "http://lardis.ru/academ/webp/pic_actor_photo_jean_louisa_kelly.webp"
    ),
    Actor(
        22,
        4,
        "Emilia Clarke",
        "http://lardis.ru/academ/webp/pic_actor_photo_emilia_clarke.webp"
    ),
    Actor(
        23,
        4,
        "Madison Ingoldsby",
        "http://lardis.ru/academ/webp/pic_actor_photo_madison_ingoldsby.webp"
    ),
    Actor(
        24,
        4,
        "Emma Thompson",
        "http://lardis.ru/academ/webp/pic_actor_photo_emma_thompson.webp"
    ),
    Actor(
        25,
        4,
        "Boris Isakovic",
        "http://lardis.ru/academ/webp/pic_actor_photo_boris_isakovich.webp"
    ),
    Actor(
        26,
        4,
        "Maxim Baldry",
        "http://lardis.ru/academ/webp/pic_actor_photo_maxim_baldry.webp"
    ),
    Actor(
        27,
        5,
        "Mario Casas",
        "http://lardis.ru/academ/webp/pic_actor_photo_mario_casas.webp"
    ),
    Actor(
        28,
        5,
        "Ana Wagener",
        "http://lardis.ru/academ/webp/pic_actor_photo_ana_wagener.webp"
    ),
    Actor(
        29,
        5,
        "Barbara Lennie",
        "http://lardis.ru/academ/webp/pic_actor_photo_barbara_lennie.webp"
    ),
    Actor(
        30,
        5,
        "Francesc Orella",
        "http://lardis.ru/academ/webp/pic_actor_photo_francesc_orella.webp"
    ),
    Actor(
        31,
        5,
        "Paco Tous",
        "http://lardis.ru/academ/webp/pic_actor_photo_paco_tous.webp"
    ),
    Actor(
        32,
        6,
        "Michael Pena",
        "http://lardis.ru/academ/webp/pic_actor_photo_michael_pena.webp"
    ),
    Actor(
        33,
        6,
        "Maggie Q",
        "http://lardis.ru/academ/webp/pic_actor_photo_maggie_q.webp"
    ),
    Actor(
        34,
        6,
        "Lucy Hale",
        "http://lardis.ru/academ/webp/pic_actor_photo_lucy_hale.webp"
    ),
    Actor(
        35,
        6,
        "Austin Stowell",
        "http://lardis.ru/academ/webp/pic_actor_photo_austin_stowell.webp"
    ),
    Actor(
        36,
        6,
        "Jimmy O. Yang",
        "http://lardis.ru/academ/webp/pic_actor_photo_jummy_o_yang.webp"
    ),
    Actor(
        37,
        6,
        "Portia Doubleday",
        "http://lardis.ru/academ/webp/pic_actor_photo_portia_doubleday.webp"
    )
)

private val listMovies = listOf<Movies>(
    Header,
    Movie(
        1,
        "Dolittle",
        101,
        87,
        2.3F,
        "Adventure, Comedy, Family",
        "PG",
        false,
        "http://lardis.ru/academ/webp/pic_movie_image_in_details_dolittle.webp",
        "http://lardis.ru/academ/webp/pic_movie_image_in_list_dolittle.webp",
        "A physician who can talk to animals embarks on an adventure to find a legendary island with a young apprentice and a crew of strange pets.",
        listActors.filter { it.idMovie == 1 }
    ),
    Movie(
        2,
        "Underwater",
        95,
        113,
        2.6F,
        "Action, Horror, Sci-Fi",
        "18+",
        true,
        "http://lardis.ru/academ/webp/pic_movie_image_in_details_underwater.webp",
        "http://lardis.ru/academ/webp/pic_movie_image_in_list_underwater.webp",
        "A crew of oceanic researchers working for a deep sea drilling company try to get to safety after a mysterious earthquake devastates their deepwater research and drilling facility located at the bottom of the Mariana Trench.",
        listActors.filter { it.idMovie == 2 }
    ),
    Movie(
        3,
        "The Call Of The Wild",
        119,
        321,
        3.4F,
        "Adventure, Drama, Family",
        "PG",
        false,
        "http://lardis.ru/academ/webp/pic_movie_image_in_details_the_call_of_the_wild.webp",
        "http://lardis.ru/academ/webp/pic_movie_image_in_list_the_call_of_the_wild.webp",
        "A sled dog struggles for survival in the wilds of the Yukon.",
        listActors.filter { it.idMovie == 3 }
    ),
    Movie(
        4,
        "Last Christmas",
        121,
        208,
        3.3F,
        "Comedy, Drama, Romance",
        "13+",
        false,
        "http://lardis.ru/academ/webp/pic_movie_image_in_details_last_christmas.webp",
        "http://lardis.ru/academ/webp/pic_movie_image_in_list_last_christmas.webp",
        "Kate is a young woman subscribed to bad decisions. Working as an elf in a year round Christmas store is not good for the wannabe singer. However, she meets Tom there. Her life takes a new turn. For Kate, it seems too good to be true.",
        listActors.filter { it.idMovie == 4 }
    ),
    Movie(
        5,
        "The Invisible Guest",
        106,
        173,
        4.0F,
        "Crime, Drama, Mystery",
        "16+",
        false,
        "http://lardis.ru/academ/webp/pic_movie_image_in_details_the_invisible_guest.webp",
        "http://lardis.ru/academ/webp/pic_movie_image_in_list_the_invisible_guest.webp",
        "A successful entrepreneur accused of murder and a witness preparation expert have less than three hours to come up with an impregnable defense.",
        listActors.filter { it.idMovie == 5 }
    ),
    Movie(
        6,
        "Fantasy Island",
        109,
        181,
        2.4F,
        "Action, Adventure, Fantasy",
        "13+",
        false,
        "http://lardis.ru/academ/webp/pic_movie_image_in_details_fantasy_island.webp",
        "http://lardis.ru/academ/webp/pic_movie_image_in_list_fantasy_island.webp",
        "When the owner and operator of a luxurious island invites a collection of guests to live out their most elaborate fantasies in relative seclusion, chaos quickly descends.",
        listActors.filter { it.idMovie == 6 }
    )
)

class FakeMovies {
    fun getListMovies() = listMovies

    fun getMoviesById(id: Int): Movie? = listMovies.filterIsInstance(Movie::class.java)
        .firstOrNull() { it -> it.id == id }

    fun getListActors(id: Int) = listActors.filter { it.idMovie == id }
}