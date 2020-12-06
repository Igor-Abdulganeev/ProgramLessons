package ru.gorinih.androidacademy.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.gorinih.androidacademy.model.Movie

class ListMoviesDiffUtils : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}