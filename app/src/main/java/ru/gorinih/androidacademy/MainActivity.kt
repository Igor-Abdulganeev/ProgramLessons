package ru.gorinih.androidacademy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.gorinih.androidacademy.ui.FragmentMovieDetails
import ru.gorinih.androidacademy.ui.FragmentMoviesList

class MainActivity : AppCompatActivity(), FragmentMoviesList.ClickFragment {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_ui,
                    FragmentMoviesList(),
                    MOVIES_FRAGMENT_TAG
                )
                .commit()
        }
    }

    override fun onMovieClick(id: Int) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_ui,
                FragmentMovieDetails.newInstance(id),
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
