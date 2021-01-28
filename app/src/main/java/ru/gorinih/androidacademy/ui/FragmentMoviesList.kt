package ru.gorinih.androidacademy.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.adapter.ListMoviesRecyclerViewAdapter
import ru.gorinih.androidacademy.data.db.MoviesDatabase
import ru.gorinih.androidacademy.databinding.FragmentMoviesListBinding
import ru.gorinih.androidacademy.viewmodel.MoviesViewModel
import ru.gorinih.androidacademy.viewmodel.MoviesViewModelFactory

class FragmentMoviesList : Fragment(), ListMoviesScrollListener.AddNewList {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private var listenerClickFragment: ClickFragment? = null
    private lateinit var viewModel: MoviesViewModel

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

    @ExperimentalSerializationApi
    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ListMoviesRecyclerViewAdapter {
            listenerClickFragment?.onMovieClick(it)
        }
        val viewModelFactory = MoviesViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory).get(MoviesViewModel::class.java)

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
/*
        lifecycleScope.launch {
            viewModel.flowMovie.collect {
                adapter.submitList(it)
            }
        }
*/
        viewModel.movieList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        binding.listMovies.addOnScrollListener(
            ListMoviesScrollListener(
                context = this,
                expectedVisibleThreshold = 6
            )
        )
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

    interface ClickFragment {
        fun onMovieClick(id: Int)
    }

    override fun onAddMovies() {
        viewModel.nextMovies()
    }

}