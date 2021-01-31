package ru.gorinih.androidacademy.presentation.ui.movies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.gorinih.androidacademy.R

class HeaderViewHolder private constructor(view: View) : MoviesListViewHolder(view) {

    companion object {
        fun from(parent: ViewGroup): MoviesListViewHolder {
            return HeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.header_view_holder_item, parent, false)
            )
        }
    }
}