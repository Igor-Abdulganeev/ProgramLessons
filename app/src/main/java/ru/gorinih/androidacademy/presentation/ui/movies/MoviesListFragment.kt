package ru.gorinih.androidacademy.presentation.ui.movies

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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.presentation.ui.movies.adapter.LoadsStateAdapter
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.presentation.ui.movies.adapter.MoviesListRecyclerViewAdapter
import ru.gorinih.androidacademy.databinding.FragmentMoviesListBinding
import ru.gorinih.androidacademy.presentation.ui.movies.utils.MoviesListScrollListener
import ru.gorinih.androidacademy.presentation.ui.movies.viewmodel.MoviesViewModel
import ru.gorinih.androidacademy.presentation.ui.movies.viewmodel.MoviesViewModelFactory

@FlowPreview
@ExperimentalSerializationApi
@InternalCoroutinesApi
class MoviesListFragment : Fragment(), MoviesListScrollListener.AddNewList {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!

    private var listenerClickFragment: ClickFragment? = null

    private lateinit var viewModel: MoviesViewModel

    private lateinit var adapterList: MoviesListRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMoviesListBinding.inflate(inflater, container, false)
        .run {
            _binding = this
            binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initViewModel()
        observeMovies()
/*
        viewModel.movieList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        binding.listMovies.addOnScrollListener(
            ListMoviesScrollListener(
                context = this,
                expectedVisibleThreshold = 6
            )
        )
*/
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onAddMovies() {
        viewModel.nextMovies()
    }

    private fun observeMovies() {
        lifecycleScope.launch {
            viewModel.getMovies().collect {
                adapterList.submitData(it)
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            MoviesViewModelFactory(requireContext())
        ).get(MoviesViewModel::class.java)
    }

    private fun initRecyclerView() {
        initAdapter()
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
    }

    private fun initAdapter() {
        adapterList = MoviesListRecyclerViewAdapter {
            listenerClickFragment?.onMovieClick(it)
        }
        binding.listMovies.adapter = adapterList.withLoadStateFooter(
            footer = LoadsStateAdapter { adapterList.retry() }
        )
    }

    private fun getSpanCount(): Int {
        return when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> resources.getInteger(R.integer.count_portrait_column)
            Configuration.ORIENTATION_LANDSCAPE -> resources.getInteger(R.integer.count_landscape_column)
            else -> resources.getInteger(R.integer.count_portrait_column)
        }
    }

    companion object {
        fun newInstance() = MoviesListFragment()
    }
}

interface ClickFragment {
    fun onMovieClick(id: Int)
}

