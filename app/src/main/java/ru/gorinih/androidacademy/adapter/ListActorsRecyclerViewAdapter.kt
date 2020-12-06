package ru.gorinih.androidacademy.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.gorinih.androidacademy.model.Actor

class ListActorsRecyclerViewAdapter :
    ListAdapter<Actor, ListActorsViewHolder>(ListActorsDiffUtils()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListActorsViewHolder {
        return ListActorsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ListActorsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}