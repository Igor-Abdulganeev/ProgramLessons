package ru.gorinih.androidacademy.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gorinih.androidacademy.adapter.ListMoviesRecyclerViewAdapter.Companion.TYPE_HEADER

abstract class ListMoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun from(parent: ViewGroup, viewType: Int): ListMoviesViewHolder {
            return when (viewType) {
                TYPE_HEADER -> HeaderViewHolder.from(parent)
                else -> MoviesViewHolder.from(parent)
            }
        }
    }
}