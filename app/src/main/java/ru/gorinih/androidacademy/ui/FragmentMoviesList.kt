package ru.gorinih.androidacademy.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.gorinih.androidacademy.adapter.ListMoviesRecyclerViewAdapter
import ru.gorinih.androidacademy.data.GetData
import ru.gorinih.androidacademy.databinding.FragmentMoviesListBinding
import ru.gorinih.androidacademy.model.Movies

class FragmentMoviesList : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private var listenerClickFragment: ClickFragment? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMoviesListBinding.inflate(inflater, container, false)
        .run {
            _binding = this
            binding.root
        }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ListMoviesRecyclerViewAdapter {
            listenerClickFragment?.onMovieClick(it)
        }
        val spanCount = getSpanCount()
        val layoutManager = GridLayoutManager(context, spanCount, RecyclerView.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) {
                    spanCount
                } else {
                    1
                }
            }
        }
        binding.listMovies.layoutManager = layoutManager
        binding.listMovies.adapter = adapter
        val context = requireContext()
        scope.launch {
            val listMoves = mutableListOf<Movies>()
            listMoves.add(Movies.Header)
            listMoves.addAll(GetData().getMovies(context))
            listMoves.let { adapter.submitList(it) }
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
        scope.cancel()
        super.onDetach()
    }

    interface ClickFragment {
        fun onMovieClick(id: Int)
    }

    private fun getSpanCount(): Int {
        return when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> 2
            Configuration.ORIENTATION_LANDSCAPE -> 3
            else -> 0
        }
    }

    companion object {
        fun newInstance() = FragmentMoviesList()
    }
}