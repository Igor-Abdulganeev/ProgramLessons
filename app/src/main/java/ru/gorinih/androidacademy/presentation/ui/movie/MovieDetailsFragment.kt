package ru.gorinih.androidacademy.presentation.ui.movie

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.databinding.FragmentMovieDetailsBinding
import ru.gorinih.androidacademy.presentation.ui.movie.adapters.ActorsListRecyclerViewAdapter
import ru.gorinih.androidacademy.presentation.ui.movie.viewmodel.MovieDetailsViewModel
import ru.gorinih.androidacademy.presentation.ui.movie.viewmodel.MovieDetailsViewModelFactory

class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailsViewModel: MovieDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        .run {
            _binding = this
            binding.root
        }

    @ExperimentalStdlibApi
    @InternalCoroutinesApi
    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idMovie = requireNotNull(arguments?.getInt(ID_MOVIE))
        detailsViewModel = ViewModelProvider(
            this,
            MovieDetailsViewModelFactory(requireContext())
        ).get(MovieDetailsViewModel::class.java)
        detailsViewModel.movie.observe(viewLifecycleOwner, {
            showMovie(it)
        })
        detailsViewModel.getMovie(idMovie)
    }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment_ui
            duration = 600
            scrimColor = Color.TRANSPARENT
            interpolator = OvershootInterpolator(2F)
        }
        sharedElementReturnTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment_ui
            duration = 300
            scrimColor = Color.TRANSPARENT
            interpolator = LinearInterpolator()
        }

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun showMovie(movie: Movies.Movie) {
        val actors = movie.listOfActors
        val adapter = ActorsListRecyclerViewAdapter()
        binding.apply {
            progressBar.isVisible = true
            descriptionTextView.text = movie.description
            reviewsTextView.text =
                resources.getString(R.string.reviews_text, movie.reviews.toString())
            reviewsRatingBar.rating = movie.rating / 2
            tagTextView.text =
                movie.listOfGenre.map { it.nameGenre }.sorted().joinToString(separator = ", ")
            nameMovieTextView.text = movie.nameMovie
            pgTextView.text = movie.rated
            backTextView.setOnClickListener { activity?.onBackPressed() }
            listActors.adapter = adapter
        }
        actors.let { adapter.submitList(it) }
        Glide.with(requireContext())
            .load(movie.detailPoster)
            .placeholder(R.drawable.ic_no_image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(binding.movieImageView)
    }


    companion object {
        private const val ID_MOVIE = "idMovie"
        fun newInstance(id: Int) = MovieDetailsFragment().apply {
            arguments = Bundle().apply {
                this.putInt(ID_MOVIE, id)
            }
        }
    }

}