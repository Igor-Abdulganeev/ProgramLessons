package ru.gorinih.androidacademy.presentation.ui.movies.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import ru.gorinih.androidacademy.data.models.Movies

class MoviesListRecyclerViewAdapter(private val listener: (Int) -> Unit) :
    PagingDataAdapter<Movies, MoviesListViewHolder>(MoviesListDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListViewHolder {
        return MoviesListViewHolder.from(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Movies.Header -> TYPE_HEADER
            is Movies.Movie -> TYPE_MOVIE
            else -> TYPE_HEADER
        }
    }

    override fun onBindViewHolder(holderList: MoviesListViewHolder, position: Int) {
        val item = getItem(position)
        if (holderList is MoviesViewHolder) {
            holderList.bind(item as Movies.Movie, listener)
        }
    }

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_MOVIE = 1
    }
}