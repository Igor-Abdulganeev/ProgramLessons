package ru.gorinih.androidacademy.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.gorinih.androidacademy.adapter.ListMoviesRecyclerViewAdapter
import ru.gorinih.androidacademy.databinding.FragmentMovieDetailsBinding
import ru.gorinih.androidacademy.databinding.FragmentMoviesListBinding
import ru.gorinih.androidacademy.model.Movie


class FragmentMoviesList : Fragment() {

    private lateinit var binding: FragmentMoviesListBinding
    private var listenerClickFragment: ClickFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )= FragmentMoviesListBinding.inflate(inflater, container, false)
        .run {
            binding = this
            binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ListMoviesRecyclerViewAdapter()
        binding.listMovies.adapter = adapter
        val listMoves = fakeListMovies()
        adapter.setData(listMoves)
        binding.constraintMovieList.setOnClickListener {
            listenerClickFragment?.onMovieClick()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ClickFragment) {
            listenerClickFragment = context
        }
    }

    override fun onDetach() {
        listenerClickFragment = null
        super.onDetach()
    }

    interface ClickFragment {
        fun onMovieClick()
    }


        // get fake data
        private fun fakeListMovies(): List<Movie> =
            listOf<Movie>(
                Movie(0,
                    "Avengers: End Game",
                    137,
                    125,
                    4,
                    "Action, Adventure, Drama",
                    "13+",
                    false,"poster1"),
                Movie(1,
                    "Tenet",
                    97,
                    98,
                    5,
                    "Action, Sci-Fi, Thriller",
                    "16+",
                    true,"poster2")

            )
}