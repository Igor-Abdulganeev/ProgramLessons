package ru.gorinih.androidacademy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.adapter.ListActorsRecyclerViewAdapter
import ru.gorinih.androidacademy.data.GetData
import ru.gorinih.androidacademy.databinding.FragmentMovieDetailsBinding
import ru.gorinih.androidacademy.model.Actor
//import ru.gorinih.androidacademy.model.FakeMovies
import ru.gorinih.androidacademy.model.Movies

class FragmentMovieDetails : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

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
        val idMovie = requireNotNull(arguments?.getInt("idMovie"))

        scope.launch {
            val movie = GetData().getMoviesById(requireContext(), idMovie)
            setMovie(movie)
        }
    }

    private fun setMovie(movie: Movies.Movie) {
        binding.descriptionTextView.text = movie.description
        binding.reviewsTextView.text =
            resources.getString(R.string.reviews_text, movie.reviews.toString())
        binding.reviewsRatingBar.rating = movie.rating / 2
        var genre = ""
        movie.listOfGenre.sortedBy { it.nameGenre }.forEach { it -> genre += "${it.nameGenre} " }
        binding.tagTextView.text = genre
        binding.nameMovieTextView.text = movie.nameMovie
        Glide.with(requireContext())
            .load(movie.detailPoster)
            .placeholder(R.drawable.ic_no_image)
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
        job.cancel()
        super.onDestroy()
    }

    companion object {
        fun newInstance(id: Int): FragmentMovieDetails {
            val args = Bundle()
            args.putInt("idMovie", id)
            val fragment = FragmentMovieDetails()
            fragment.arguments = args
            return fragment
        }
    }

}