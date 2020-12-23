package ru.gorinih.androidacademy.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.databinding.ViewHolderMovieBinding
import ru.gorinih.androidacademy.model.Movies

class MoviesViewHolder private constructor(private val binding: ViewHolderMovieBinding) :
    ListMoviesViewHolder(binding.root) {

    fun bind(
        item: Movies.Movie,
        listener: (Int) -> Unit
    ) {
        binding.progressBar.isVisible = true
        binding.constraintMovieList.setOnClickListener { listener(item.id) }
        val resource = itemView.resources
        binding.nameMovieTextView.text = item.nameMovie
        binding.minuteTextView.text =
            resource.getString(R.string.movie_length, item.duration.toString())
        binding.reviewsTextView.text =
            resource.getString(R.string.reviews_text, item.reviews.toString())
        binding.reviewsRatingBar.rating = item.rating / 2
        binding.tagTextView.text =
            item.listOfGenre.map { it.nameGenre }.sorted().joinToString(separator = ", ")
        binding.pgTextView.text = item.rated
        binding.favorite.setImageResource(
            if (item.like) {
                R.drawable.ic_favorite_red_24
            } else {
                R.drawable.ic_favorite_gray_24
            }
        )
        Glide.with(itemView.context)
            .load(item.poster)
            .placeholder(R.drawable.ic_no_image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.isVisible = false
                    return false
                }
            })
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