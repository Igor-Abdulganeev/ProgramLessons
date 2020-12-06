package ru.gorinih.androidacademy.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.gorinih.androidacademy.model.Movie

class ListMoviesRecyclerViewAdapter(private val listener: (Int) -> Unit) :
    ListAdapter<Movie, ListMoviesViewHolder>(ListMoviesDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMoviesViewHolder {
        return ListMoviesViewHolder.from(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            else -> TYPE_MOVIE
        }
    }

    override fun onBindViewHolder(holder: ListMoviesViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is MoviesViewHolder -> holder.bind(item, listener)
            is HeaderViewHolder -> {
            }
        }

    }

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_MOVIE = 1
    }
}