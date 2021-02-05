package ru.gorinih.androidacademy.presentation.ui.movies.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gorinih.androidacademy.presentation.ui.movies.adapters.MoviesListRecyclerViewAdapter.Companion.TYPE_HEADER

abstract class MoviesListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun from(parent: ViewGroup, viewType: Int): MoviesListViewHolder {
            return when (viewType) {
                TYPE_HEADER -> HeaderViewHolder.from(parent)
                else -> MoviesViewHolder.from(parent)
            }
        }
    }
}