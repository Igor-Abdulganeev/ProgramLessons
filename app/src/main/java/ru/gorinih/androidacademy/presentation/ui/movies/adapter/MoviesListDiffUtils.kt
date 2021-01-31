package ru.gorinih.androidacademy.presentation.ui.movies.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.gorinih.androidacademy.data.models.Movies

class MoviesListDiffUtils : DiffUtil.ItemCallback<Movies>() {
    override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
        return if (oldItem is Movies.Movie && newItem is Movies.Movie) oldItem.id == newItem.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
        return oldItem == newItem
    }
}