package ru.gorinih.androidacademy.presentation.ui.movie

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.databinding.FragmentMovieDetailsBinding
import ru.gorinih.androidacademy.presentation.ui.movie.viewmodel.MovieDetailsViewModel
import ru.gorinih.androidacademy.presentation.ui.movie.viewmodel.MovieDetailsViewModelFactory

class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailsViewModel: MovieDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        .run {
            _binding = this
            binding.root
        }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idMovie = requireNotNull(arguments?.getInt(ID_MOVIE))
        detailsViewModel = ViewModelProvider(
            this,
            MovieDetailsViewModelFactory()
        ).get(MovieDetailsViewModel::class.java)
        detailsViewModel.movie.observe(viewLifecycleOwner, {
            showMovie(it)
        })
        detailsViewModel.getMovie(idMovie)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun showMovie(movie: Movies.Movie) {
        binding.progressBar.isVisible = true
        binding.descriptionTextView.text = movie.description
        binding.reviewsTextView.text =
            resources.getString(R.string.reviews_text, movie.reviews.toString())
        binding.reviewsRatingBar.rating = movie.rating / 2
        binding.tagTextView.text =
            movie.listOfGenre.map { it.nameGenre }.sorted().joinToString(separator = ", ")
        binding.nameMovieTextView.text = movie.nameMovie
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
        binding.pgTextView.text = movie.rated
/*
        val actors = movie.listOfActors
        val adapter = ActorsListRecyclerViewAdapter()
        binding.listActors.adapter = adapter
        actors.let { adapter.submitList(it) }
*/
        binding.backTextView.setOnClickListener { activity?.onBackPressed() }
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