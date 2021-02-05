package ru.gorinih.androidacademy.presentation.ui.movie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.data.models.Actor
import ru.gorinih.androidacademy.databinding.ActorItemViewHolderBinding

class ActorsListViewHolder(private val binding: ActorItemViewHolderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: Actor
    ) {
        with(binding) {
            actorTextView.text = item.nameActor
            Glide.with(itemView.context)
                .load(item.photoActor)
                .placeholder(R.drawable.ic_no_photo)
                .into(binding.actorImageView)
        }
    }

    companion object {
        fun from(parent: ViewGroup): ActorsListViewHolder {
            return ActorsListViewHolder(
                binding = ActorItemViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}