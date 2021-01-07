package ru.gorinih.androidacademy.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.gorinih.androidacademy.data.model.Movies

class ListMoviesRecyclerViewAdapter(private val listener: (Int) -> Unit) :
    ListAdapter<Movies, ListMoviesViewHolder>(ListMoviesDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMoviesViewHolder {
        return ListMoviesViewHolder.from(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Movies.Header -> TYPE_HEADER
            is Movies.Movie -> TYPE_MOVIE
        }
    }

    override fun onBindViewHolder(holder: ListMoviesViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is MoviesViewHolder) {
            holder.bind(item as Movies.Movie, listener)
        }
    }

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_MOVIE = 1
    }
}