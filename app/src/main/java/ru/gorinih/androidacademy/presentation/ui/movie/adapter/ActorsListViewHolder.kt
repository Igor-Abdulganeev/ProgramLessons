package ru.gorinih.androidacademy.presentation.ui.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.data.models.Actor
import ru.gorinih.androidacademy.databinding.ActorViewHolderItemBinding

class ActorsListViewHolder private constructor(private val binding: ActorViewHolderItemBinding) :
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
        fun from(parent: ViewGroup): ActorsListViewHolder {
            return ActorsListViewHolder(
                ActorViewHolderItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
