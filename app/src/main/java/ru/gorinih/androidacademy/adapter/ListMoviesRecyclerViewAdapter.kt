package ru.gorinih.androidacademy.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gorinih.androidacademy.model.Movie

class ListMoviesRecyclerViewAdapter : RecyclerView.Adapter<ListMoviesViewHolder>() {

    private var movies = listOf<Movie>()

    fun setData(items: List<Movie>){
     movies = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMoviesViewHolder {
        return ListMoviesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ListMoviesViewHolder, position: Int) {
        val item = movies[position]
        holder.bind(item)
    }


    override fun getItemCount() = movies.size

}