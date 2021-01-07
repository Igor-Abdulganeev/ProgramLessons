package ru.gorinih.androidacademy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.data.model.Actor
import ru.gorinih.androidacademy.databinding.ViewHolderActorBinding

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