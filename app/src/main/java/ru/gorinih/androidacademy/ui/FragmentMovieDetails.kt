package ru.gorinih.androidacademy.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.adapter.ListActorsRecyclerViewAdapter
import ru.gorinih.androidacademy.data.Movies
import ru.gorinih.androidacademy.databinding.FragmentMovieDetailsBinding
import ru.gorinih.androidacademy.model.MoviesViewModel
import ru.gorinih.androidacademy.model.MoviesViewModelFactory

class FragmentMovieDetails : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MoviesViewModel
    private lateinit var viewModelFactory: MoviesViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        .run {
            _binding = this
            binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idMovie = requireNotNull(arguments?.getInt(ID_MOVIE))

        viewModelFactory = MoviesViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory).get(MoviesViewModel::class.java)
        viewModel.getMovieById(idMovie)
        viewModel.movie.observe(viewLifecycleOwner, {
            showMovie(it)
        })
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
            .load(movie.detailPoster + "5")
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
        val actors = movie.listOfActors
        val adapter = ListActorsRecyclerViewAdapter()
        binding.listActors.adapter = adapter
        actors.let { adapter.submitList(it) }
        binding.backTextView.setOnClickListener { activity?.onBackPressed() }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        private const val ID_MOVIE = "idMovie"
        fun newInstance(id: Int) = FragmentMovieDetails().apply {
            arguments = Bundle().apply {
                this.putInt(ID_MOVIE, id)
            }
        }
    }

}