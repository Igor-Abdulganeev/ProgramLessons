package ru.gorinih.androidacademy.presentation.ui.movie.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.gorinih.androidacademy.data.models.Actor

class ActorsListRecyclerViewAdapter :
    ListAdapter<Actor, ActorsListViewHolder>(ActorsListDiffUtils()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsListViewHolder {
        return ActorsListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: ActorsListViewHolder, position: Int) {
        holderList.bind(getItem(position))
    }
}