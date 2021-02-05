package ru.gorinih.androidacademy.presentation.ui.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.gorinih.androidacademy.R

class HeaderViewHolder private constructor(view: View) : MoviesListViewHolder(view) {

    companion object {
        fun from(parent: ViewGroup): MoviesListViewHolder {
            return HeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.header_item_view_holder, parent, false)
            )
        }
    }
}