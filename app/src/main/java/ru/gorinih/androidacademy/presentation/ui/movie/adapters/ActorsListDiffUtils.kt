package ru.gorinih.androidacademy.presentation.ui.movie.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.gorinih.androidacademy.data.models.Actor

class ActorsListDiffUtils : DiffUtil.ItemCallback<Actor>() {
    override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem == newItem
    }
}