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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.databinding.FragmentMoviesListBinding
import ru.gorinih.androidacademy.presentation.ui.movies.adapters.MoviesListRecyclerViewAdapter
import ru.gorinih.androidacademy.presentation.ui.movies.paging.MoviesLoadStateAdapter
import ru.gorinih.androidacademy.presentation.ui.movies.viewmodel.MoviesViewModel
import ru.gorinih.androidacademy.presentation.ui.movies.viewmodel.MoviesViewModelFactory

class MoviesListFragment : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterList: MoviesListRecyclerViewAdapter

    private var listenerClickFragment: ClickFragment? = null

    @InternalCoroutinesApi
    private lateinit var viewModel: MoviesViewModel

    private var jobMovies: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMoviesListBinding.inflate(inflater, container, false)
        .run {
            _binding = this
            binding.root
        }

    @FlowPreview
    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(this, MoviesViewModelFactory()).get(MoviesViewModel::class.java)
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

        adapterList = MoviesListRecyclerViewAdapter {
            listenerClickFragment?.onMovieClick(it)
        }
        binding.listMovies.adapter = adapterList.withLoadStateHeaderAndFooter(
            header = MoviesLoadStateAdapter { adapterList.retry() },
            footer = MoviesLoadStateAdapter { adapterList.retry() }
        )
        jobMovies?.cancel()
        jobMovies = lifecycleScope.launch {
            viewModel.getMovies().collectLatest {
                adapterList.submitData(it)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ClickFragment)
            listenerClickFragment = context
    }

    override fun onDetach() {
        listenerClickFragment = null
        super.onDetach()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
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
