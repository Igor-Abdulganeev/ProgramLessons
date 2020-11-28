package ru.gorinih.androidacademy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ru.gorinih.androidacademy.databinding.ActivityMainBinding
import ru.gorinih.androidacademy.ui.FragmentMoviesDetails
import ru.gorinih.androidacademy.ui.FragmentMoviesList

class MainActivity : AppCompatActivity(), FragmentMoviesList.ClickFragment {
    private lateinit var binding: ActivityMainBinding
    private var currentTag = MOVIES_FRAGMENT_TAG
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        currentTag = savedInstanceState?.getString(KEY_FRAGMENT_TAG) ?: MOVIES_FRAGMENT_TAG
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_ui,
                supportFragmentManager.findFragmentByTag(currentTag) ?: FragmentMoviesList(),
                currentTag
            )
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_FRAGMENT_TAG, currentTag)
    }

    override fun onMovieClick() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_ui, FragmentMoviesDetails(), MOVIE_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
        currentTag = MOVIE_FRAGMENT_TAG
    }

    override fun onBackPressed() {
        super.onBackPressed()
        currentTag = MOVIES_FRAGMENT_TAG
    }

    companion object {
        const val MOVIES_FRAGMENT_TAG = "movies.fragment.tag"
        const val MOVIE_FRAGMENT_TAG = "movie.fragment.tag"
        const val KEY_FRAGMENT_TAG = "fragment.tag.name"
    }

}
