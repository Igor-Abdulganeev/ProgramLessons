package ru.gorinih.androidacademy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ru.gorinih.androidacademy.databinding.ActivityMainBinding
import ru.gorinih.androidacademy.model.Movie
import ru.gorinih.androidacademy.ui.FragmentMovieDetails
import ru.gorinih.androidacademy.ui.FragmentMoviesList

class MainActivity : AppCompatActivity(), FragmentMoviesList.ClickFragment {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

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

    override fun onMovieClick() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_ui,
                FragmentMovieDetails(),
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
