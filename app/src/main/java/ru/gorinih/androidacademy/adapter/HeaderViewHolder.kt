package ru.gorinih.androidacademy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.gorinih.androidacademy.R

class HeaderViewHolder private constructor(view: View) : ListMoviesViewHolder(view) {

    companion object {
        fun from(parent: ViewGroup): ListMoviesViewHolder {
            return HeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_movie_header, parent, false)
            )
        }
    }
}