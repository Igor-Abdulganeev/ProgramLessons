package ru.gorinih.androidacademy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gorinih.androidacademy.BuildConfig
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.model.Movie

class ListMoviesRecyclerViewAdapter : RecyclerView.Adapter<ListMoviesViewHolder>() {

    private var movies = listOf<Movie>()

    fun setData(items: List<Movie>){
     movies = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMoviesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.view_holder_movie, parent, false)
        return ListMoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListMoviesViewHolder, position: Int) {
        val item = movies[position]
        val packageName= holder.itemView.context.packageName
        //BuildConfig.APPLICATION_ID
        val resource= holder.itemView.resources
        holder.nameMovie.text= item.nameMovie
        holder.movieDuration.text= resource.getString(R.string.movie_length,item.movieDuration.toString())
        holder.reviews.text= resource.getString(R.string.reviews_text,item.reviews.toString())
        holder.rating.rating= item.rating.toFloat()
        holder.movieGenre.text= item.movieGenre
        holder.rated.text= item.rated
        holder.like.setImageResource(when(item.like){
            true-> R.drawable.ic_favorite_red_24
            false->R.drawable.ic_favorite_gray_24
        })
        val id= resource.getIdentifier(item.poster,"drawable",packageName)
        val image = resource.getDrawable(id, resource.newTheme())
        holder.poster.setImageDrawable(image)
    }

    override fun getItemCount() = movies.size
}