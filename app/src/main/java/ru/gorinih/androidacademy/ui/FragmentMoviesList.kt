package ru.gorinih.androidacademy.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.gorinih.androidacademy.databinding.FragmentMoviesListBinding


class FragmentMoviesList : Fragment() {

    private lateinit var binding: FragmentMoviesListBinding
    private var listenerClickFragment: ClickFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMoviesListBinding.inflate(inflater, container, false).run {
        binding = this
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.constraintMovieList.setOnClickListener {
            listenerClickFragment?.onMovieClick()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listenerClickFragment = context as ClickFragment
    }

    interface ClickFragment {
        fun onMovieClick()
    }
}