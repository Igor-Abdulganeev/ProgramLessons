package ru.gorinih.androidacademy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.gorinih.androidacademy.databinding.FragmentMovieDetailsBinding

class FragmentMovieDetails : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
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
        binding.constraintMovieDetails.setOnClickListener { activity?.onBackPressed() }
    }

}