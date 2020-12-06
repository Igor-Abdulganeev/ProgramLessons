package ru.gorinih.androidacademy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.model.Actor

class ListActorsViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {
    private val nameActor: TextView = view.findViewById(R.id.actor_text_view)
    private val photoActor: ImageView = view.findViewById(R.id.actor_image_view)

    fun bind(
        item: Actor
    ) {
        nameActor.text = item.nameActor
        Glide.with(itemView.context)
            .load(item.photoActor)
            .placeholder(R.drawable.ic_no_photo)
            .into(photoActor)
    }

    companion object {
        fun from(parent: ViewGroup): ListActorsViewHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_actor, parent, false)
            return ListActorsViewHolder(view)
        }
    }
}