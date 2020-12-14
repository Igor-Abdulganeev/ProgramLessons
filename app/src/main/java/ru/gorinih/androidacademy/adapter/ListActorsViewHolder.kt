package ru.gorinih.androidacademy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.databinding.ViewHolderActorBinding
import ru.gorinih.androidacademy.databinding.ViewHolderMovieBinding
import ru.gorinih.androidacademy.model.Actor

class ListActorsViewHolder private constructor(private val binding: ViewHolderActorBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: Actor
    ) {
        binding.actorTextView.text = item.nameActor
        Glide.with(itemView.context)
            .load(item.photoActor)
            .placeholder(R.drawable.ic_no_photo)
            .into(binding.actorImageView)
    }

    companion object {
        fun from(parent: ViewGroup): ListActorsViewHolder {
            return ListActorsViewHolder(
                ViewHolderActorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }
}