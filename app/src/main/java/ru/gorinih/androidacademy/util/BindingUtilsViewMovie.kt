package ru.gorinih.androidacademy.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import ru.gorinih.androidacademy.model.Movie

@BindingAdapter("nameMovieTextView")
fun TextView.setNameMovie(item: Movie) {
    text = item.nameMovie
}
