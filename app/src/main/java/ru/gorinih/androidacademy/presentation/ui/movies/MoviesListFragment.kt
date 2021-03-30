package ru.gorinih.androidacademy.presentation.ui.movies

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.AppMain
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.databinding.FragmentMoviesListBinding
import ru.gorinih.androidacademy.presentation.ui.movies.adapters.MoviesListRecyclerViewAdapter
import ru.gorinih.androidacademy.presentation.ui.movies.paging.MoviesLoadStateAdapter
import ru.gorinih.androidacademy.presentation.ui.movies.viewmodel.MoviesViewModel
import javax.inject.Inject

@FlowPreview
@InternalCoroutinesApi
@ExperimentalSerializationApi
@ExperimentalCoroutinesApi
class MoviesListFragment : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterList: MoviesListRecyclerViewAdapter

    private var listenerClickFragment: ClickFragment? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MoviesViewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initViewModel()  using dagger
        viewModel = ViewModelProvider(this, viewModelFactory)[MoviesViewModel::class.java]
        initAdapter()
        observeData()
        postTransition(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialElevationScale(false).apply {
            duration = 500L
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 1100L
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ClickFragment)
            listenerClickFragment = context
        (requireActivity().application as AppMain).appMain.inject(this)
    }

    override fun onDetach() {
        jobMovies?.cancel()
        listenerClickFragment = null
        super.onDetach()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun observeData() {
        binding.retryButton.setOnClickListener { adapterList.retry() }
        jobMovies?.cancel()
        jobMovies = lifecycleScope.launch {
            viewModel.getMovies().collectLatest {
                adapterList.submitData(it)
/*
                    .map {
                        if (it is Movies.Movie) {
                            if (it.idDb == 1 ) {
                                Movies.Header as Movies
                            }
                            else it
                        }
                        else it
                    }
*/
            }
        }
    }

    private fun initAdapter() {
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

        adapterList = MoviesListRecyclerViewAdapter { id, view ->
            listenerClickFragment?.onMovieClick(id, view)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            adapterList.loadStateFlow.collectLatest {
                binding.mainProgressBar.isVisible =
                    it.source.refresh is LoadState.Loading
                binding.retryButton.isVisible =
                    it.source.refresh is LoadState.Error
            }
        }

        binding.listMovies.adapter = adapterList.withLoadStateHeaderAndFooter(
            header = MoviesLoadStateAdapter { adapterList.retry() },
            footer = MoviesLoadStateAdapter { adapterList.retry() }
        )
    }

    private fun postTransition(view: View) {
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

/*
    private fun initViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                MoviesViewModelFactory(requireContext())
            ).get(MoviesViewModel::class.java)
    }
*/

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
    fun onMovieClick(id: Int, view: View)
}
