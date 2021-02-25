package ru.gorinih.androidacademy.presentation

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentManager
import androidx.work.WorkManager
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.presentation.ui.movie.MovieDetailsFragment
import ru.gorinih.androidacademy.presentation.ui.movies.ClickFragment
import ru.gorinih.androidacademy.presentation.ui.movies.MoviesListFragment
import ru.gorinih.androidacademy.services.MoviesRepoWorker

class MainActivity : AppCompatActivity(), ClickFragment {

    @SuppressLint("RestrictedApi")
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
            if (intent != null) {
                intent?.let(::startIntent)
            }
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val idMovie = intent.getIntExtra(getString(R.string.name_intent), 0)
            if (idMovie != 0) {
                onMovieClick(idMovie, true)
            }
        }
    }

    private fun startIntent(intent: Intent) {
        val idMovie = intent.getIntExtra(getString(R.string.name_intent), 0)
        if (idMovie != 0) {
            onMovieClick(idMovie, true)
        }
    }

    override fun onMovieClick(id: Int, startNotify: Boolean) {
        supportFragmentManager.popBackStack(
            MOVIES_FRAGMENT_TAG,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        supportFragmentManager.beginTransaction().apply {
            replace(
                R.id.fragment_ui,
                MovieDetailsFragment.newInstance(id),
                MOVIE_FRAGMENT_TAG
            )
            addToBackStack(null)
            commit()
        }
    }

    companion object {
        const val MOVIES_FRAGMENT_TAG = "MoviesListFragment"
        const val MOVIE_FRAGMENT_TAG = "MovieDetailsFragment"
    }
}
