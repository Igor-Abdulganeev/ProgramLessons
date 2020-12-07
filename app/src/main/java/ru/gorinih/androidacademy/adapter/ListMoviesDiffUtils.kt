package ru.gorinih.androidacademy.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.gorinih.androidacademy.model.Movies

class ListMoviesDiffUtils : DiffUtil.ItemCallback<Movies>() {
    override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
        return if (oldItem is Movies.Movie && newItem is Movies.Movie) oldItem.id == newItem.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
        return oldItem == newItem
    }
}