package ru.gorinih.androidacademy.presentation.ui.movies.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.imageview.ShapeableImageView
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.databinding.MovieItemViewHolderBinding

class MoviesViewHolder private constructor(private val binding: MovieItemViewHolderBinding) :
    MoviesListViewHolder(binding.root) {

    fun bind(
        item: Movies.Movie,
        listener: (Int, View) -> Unit
    ) {
        val resource = itemView.resources
        with(binding) {
            progressBar.isVisible = true
            constraintMovieList.setOnClickListener { listener(item.id, constraintMovieList) }
            ViewCompat.setTransitionName(constraintMovieList, item.id.toString())
            nameMovieTextView.text = item.nameMovie
            minuteTextView.text =
                resource.getString(R.string.movie_length, item.duration.toString())
            reviewsTextView.text =
                resource.getString(R.string.reviews_text, item.reviews.toString())
            reviewsRatingBar.rating = item.rating / 2
            tagTextView.text =
                item.listOfGenre.map { it.nameGenre }.sorted().joinToString(separator = ", ")
            pgTextView.text = item.rated
            favorite.setImageResource(
                if (item.likes) {
                    R.drawable.ic_favorite_red_24
                } else {
                    R.drawable.ic_favorite_gray_24
                }
            )
        }
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
                    binding.progressBar.isVisible = false
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
        fun from(parent: ViewGroup): MoviesListViewHolder {
            return MoviesViewHolder(
                MovieItemViewHolderBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }
}
