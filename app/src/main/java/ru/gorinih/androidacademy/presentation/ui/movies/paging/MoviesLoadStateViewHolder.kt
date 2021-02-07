package ru.gorinih.androidacademy.presentation.ui.movies.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.databinding.MovieLoadsStateViewHolderBinding

class MoviesLoadStateViewHolder(
    private val binding: MovieLoadsStateViewHolderBinding,
    private val retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) binding.errorMsg.text = loadState.error.localizedMessage

        with(binding) {
            errorMsg.isVisible = loadState !is LoadState.Loading
            progressBar.isVisible = loadState is LoadState.Loading
            retryButton.isVisible = loadState !is LoadState.Loading
            retryButton.setOnClickListener { retry.invoke() }
        }
    }

    companion object {
        fun newInstance(
            parent: ViewGroup,
            retry: () -> Unit
        ): MoviesLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_loads_state_view_holder, parent, false)
            val item = MovieLoadsStateViewHolderBinding.bind(view)
            return MoviesLoadStateViewHolder(item, retry)
        }
    }
}