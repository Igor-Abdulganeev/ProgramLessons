package ru.gorinih.androidacademy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.databinding.ViewHolderMovieBinding
import ru.gorinih.androidacademy.model.Movies

class MoviesViewHolder private constructor(private val binding: ViewHolderMovieBinding) :
    ListMoviesViewHolder(binding.root) {

    fun bind(
        item: Movies.Movie,
        listener: (Int) -> Unit
    ) {
        binding.constraintMovieList.setOnClickListener { listener(item.id) }
        val resource = itemView.resources
        binding.nameMovieTextView.text = item.nameMovie
        binding.minuteTextView.text =
            resource.getString(R.string.movie_length, item.movieDuration.toString())
        binding.reviewsTextView.text =
            resource.getString(R.string.reviews_text, item.reviews.toString())
        binding.reviewsRatingBar.rating = item.rating
        binding.tagTextView.text = item.movieGenre
        binding.pgTextView.text = item.rated
        binding.favorite.setImageResource(
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
            .into(binding.movieImageView)
    }

    companion object {
        fun from(parent: ViewGroup): ListMoviesViewHolder {
            return MoviesViewHolder(
                ViewHolderMovieBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }
}