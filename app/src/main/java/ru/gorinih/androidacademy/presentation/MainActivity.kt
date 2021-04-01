package ru.gorinih.androidacademy.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.AppMain
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.presentation.ui.movie.MovieDetailsFragment
import ru.gorinih.androidacademy.presentation.ui.movies.ClickFragment
import ru.gorinih.androidacademy.presentation.ui.movies.MoviesListFragment

@ExperimentalCoroutinesApi
@SuppressLint("RestrictedApi")
@ExperimentalSerializationApi
@FlowPreview
@InternalCoroutinesApi
class MainActivity : AppCompatActivity(), ClickFragment {

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
            if (intent != null) {
                intent?.let(::startIntent)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val idMovie = intent.getIntExtra(getString(R.string.name_intent), 0)
            if (idMovie != 0) {
                val view: View = requireViewById(R.id.fragment_ui)
                ViewCompat.setTransitionName(view, idMovie.toString())
                onMovieClick(idMovie, view)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun startIntent(intent: Intent) {
        val idMovie = intent.getIntExtra(getString(R.string.name_intent), 0)
        if (idMovie != 0) {
            val view: View = requireViewById(R.id.fragment_ui)
            ViewCompat.setTransitionName(view, idMovie.toString())
            onMovieClick(idMovie, view)
        }
    }

    override fun onMovieClick(id: Int, view: View) {
        supportFragmentManager.popBackStack(
            MOVIES_FRAGMENT_TAG,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        supportFragmentManager.beginTransaction().apply {
            addSharedElement(view, getString(R.string.show_movie_name_transition))
            replace(
                R.id.fragment_ui,
                MovieDetailsFragment.newInstance(id),
                MOVIE_FRAGMENT_TAG
            )
            addToBackStack(MOVIE_FRAGMENT_TAG) //null
            commit()
        }
    }

    companion object {
        const val MOVIES_FRAGMENT_TAG = "MoviesListFragment"
        const val MOVIE_FRAGMENT_TAG = "MovieDetailsFragment"
    }
}
