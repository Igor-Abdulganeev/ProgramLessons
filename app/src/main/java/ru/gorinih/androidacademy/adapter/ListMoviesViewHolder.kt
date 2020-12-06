package ru.gorinih.androidacademy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.adapter.ListMoviesRecyclerViewAdapter.Companion.TYPE_HEADER

abstract class ListMoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun from(parent: ViewGroup, viewType: Int): ListMoviesViewHolder {
            return when (viewType) {
                TYPE_HEADER -> HeaderViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_holder_movie_header, parent, false)
                )
                else -> MoviesViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_holder_movie, parent, false)
                )
            }
        }
    }

}