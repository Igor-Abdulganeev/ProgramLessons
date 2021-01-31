package ru.gorinih.androidacademy.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.presentation.ui.movie.MovieDetailsFragment
import ru.gorinih.androidacademy.presentation.ui.movies.ClickFragment
import ru.gorinih.androidacademy.presentation.ui.movies.MoviesListFragment

class MainActivity : AppCompatActivity(), ClickFragment {

    @ExperimentalSerializationApi
    @FlowPreview
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_ui,
                    MoviesListFragment.newInstance(),
                    MOVIES_FRAGMENT_TAG
                )
                .commit()
        }
    }

    override fun onMovieClick(id: Int) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_ui,
                MovieDetailsFragment.newInstance(id),
                MOVIE_FRAGMENT_TAG
            )
            .addToBackStack(null)
            .commit()
    }

    companion object {
        const val MOVIES_FRAGMENT_TAG = "movies.fragment.tag"
        const val MOVIE_FRAGMENT_TAG = "movie.fragment.tag"
    }
}
