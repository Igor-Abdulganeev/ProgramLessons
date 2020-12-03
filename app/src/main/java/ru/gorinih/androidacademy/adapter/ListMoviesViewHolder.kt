package ru.gorinih.androidacademy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.model.Movie

class ListMoviesViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {
    private val nameMovie: TextView = view.findViewById(R.id.name_movie_text_view)
    private val movieDuration: TextView = view.findViewById(R.id.minute_text_view)
    private val reviews: TextView = view.findViewById(R.id.reviews_text_view)
    private val rating: RatingBar = view.findViewById(R.id.reviews_rating_bar)
    private val movieGenre: TextView = view.findViewById(R.id.tag_text_view)
    private val rated: TextView = view.findViewById(R.id.pg_text_view)
    private val like: ImageView = view.findViewById(R.id.favorite)
    private val poster: ImageView = view.findViewById(R.id.movie_image_view)

    fun bind(
        item: Movie
    ) {
        val packageName = itemView.context.packageName
        //BuildConfig.APPLICATION_ID
        val resource = itemView.resources
        nameMovie.text = item.nameMovie
        movieDuration.text =
            resource.getString(R.string.movie_length, item.movieDuration.toString())
        reviews.text = resource.getString(R.string.reviews_text, item.reviews.toString())
        rating.rating = item.rating.toFloat()
        movieGenre.text = item.movieGenre
        rated.text = item.rated
        like.setImageResource(
            when (item.like) {
                true -> R.drawable.ic_favorite_red_24
                false -> R.drawable.ic_favorite_gray_24
            }
        )
        val id = resource.getIdentifier(item.poster, "drawable", packageName)
        val image = resource.getDrawable(id, resource.newTheme())
        poster.setImageDrawable(image)
    }

    companion object {
        fun from(parent: ViewGroup): ListMoviesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.view_holder_movie, parent, false)
            return ListMoviesViewHolder(view)
        }
    }

}