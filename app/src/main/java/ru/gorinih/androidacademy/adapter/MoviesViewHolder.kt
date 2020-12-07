package ru.gorinih.androidacademy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.model.Movies

class MoviesViewHolder private constructor(view: View) : ListMoviesViewHolder(view) {
    private val nameMovie: TextView = view.findViewById(R.id.name_movie_text_view)
    private val movieDuration: TextView = view.findViewById(R.id.minute_text_view)
    private val reviews: TextView = view.findViewById(R.id.reviews_text_view)
    private val rating: RatingBar = view.findViewById(R.id.reviews_rating_bar)
    private val movieGenre: TextView = view.findViewById(R.id.tag_text_view)
    private val rated: TextView = view.findViewById(R.id.pg_text_view)
    private val like: ImageView = view.findViewById(R.id.favorite)
    private val poster: ImageView = view.findViewById(R.id.movie_image_view)
    private val background: ConstraintLayout = view.findViewById(R.id.constraint_movie_list)

    fun bind(
        item: Movies.Movie,
        listener: (Int) -> Unit
    ) {
        background.setOnClickListener { listener(item.id) }
        val resource = itemView.resources
        nameMovie.text = item.nameMovie
        movieDuration.text =
            resource.getString(R.string.movie_length, item.movieDuration.toString())
        reviews.text = resource.getString(R.string.reviews_text, item.reviews.toString())
        rating.rating = item.rating
        movieGenre.text = item.movieGenre
        rated.text = item.rated
        like.setImageResource(
            if (item.like) {
                R.drawable.ic_favorite_red_24
            } else {
                R.drawable.ic_favorite_gray_24
            }
        )
        Glide.with(itemView.context)
            .asBitmap()
            .load(item.poster)
            .placeholder(R.drawable.ic_no_image)
            .into(poster)
    }

    companion object {
        fun from(parent: ViewGroup): ListMoviesViewHolder {
            return MoviesViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_movie, parent, false)
            )
        }
    }
}