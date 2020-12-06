package ru.gorinih.androidacademy.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gorinih.androidacademy.adapter.ListMoviesRecyclerViewAdapter
import ru.gorinih.androidacademy.databinding.FragmentMoviesListBinding
import ru.gorinih.androidacademy.model.FakeMovies


class FragmentMoviesList : Fragment() {

    private lateinit var binding: FragmentMoviesListBinding
    private var listenerClickFragment: ClickFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMoviesListBinding.inflate(inflater, container, false)
        .run {
            binding = this
            binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listMoves = FakeMovies().getListMovies()
        val adapter = ListMoviesRecyclerViewAdapter {
            listenerClickFragment?.onMovieClick(it)
        }

        val layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) {
                    2
                } else {
                    1
                }
            }
        }

        binding.listMovies.layoutManager = layoutManager
        binding.listMovies.adapter = adapter
        listMoves.let { adapter.submitList(it) }
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
        fun onMovieClick(id: Int)
    }

}