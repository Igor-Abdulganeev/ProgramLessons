package ru.gorinih.androidacademy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.adapter.ListActorsRecyclerViewAdapter
import ru.gorinih.androidacademy.databinding.FragmentMovieDetailsBinding
import ru.gorinih.androidacademy.model.FakeMovies
import ru.gorinih.androidacademy.model.Movie

class FragmentMovieDetails : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var movie: Movie
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        .run {
            binding = this
            binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idMovie = arguments?.getInt("idMovie") ?: 0
        movie = FakeMovies().getMoviesById(idMovie)
        val actors = FakeMovies().getListActors(movie.id)
        val adapter = ListActorsRecyclerViewAdapter()
        binding.listActors.adapter = adapter
        actors.let { adapter.submitList(it) }
        binding.movie = movie
        Glide.with(requireContext())
            .load(movie.detailPoster)
            .placeholder(R.drawable.ic_no_image)
            .into(binding.movieImageView)
        binding.backTextView.setOnClickListener { activity?.onBackPressed() }
    }

    companion object {
        fun instanceFragmentMovie(id: Int): FragmentMovieDetails {
            val args = Bundle()
            args.putInt("idMovie", id)
            val fragment = FragmentMovieDetails()
            fragment.arguments = args
            return fragment
        }
    }

}