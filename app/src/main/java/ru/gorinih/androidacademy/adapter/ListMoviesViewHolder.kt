package ru.gorinih.androidacademy.adapter

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gorinih.androidacademy.R

class ListMoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val nameMovie: TextView = view.findViewById(R.id.name_movie_text_view)
    val movieDuration: TextView = view.findViewById(R.id.minute_text_view)
    val reviews: TextView = view.findViewById(R.id.reviews_text_view)
    val rating: RatingBar = view.findViewById(R.id.reviews_rating_bar)
    val movieGenre: TextView = view.findViewById(R.id.tag_text_view)
    val rated: TextView = view.findViewById(R.id.pg_text_view)
    val like: ImageView = view.findViewById(R.id.favorite)
    val poster: ImageView = view.findViewById(R.id.movie_image_view)
}