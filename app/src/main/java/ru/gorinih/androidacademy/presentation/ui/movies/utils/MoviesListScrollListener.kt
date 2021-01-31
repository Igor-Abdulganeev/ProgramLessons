package ru.gorinih.androidacademy.presentation.ui.movies.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MoviesListScrollListener(
    private val context: AddNewList,
    private val expectedVisibleThreshold: Int
) : RecyclerView.OnScrollListener() {
    private var loading = true
    private var previousTotalItemCount = 0
    private var visibleItemCount = 0
    private var nowTotalItemCount = 0
    private var firstVisibleItem = 0
    private var addNewList: AddNewList? = null

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager: GridLayoutManager = recyclerView.layoutManager as GridLayoutManager
        addNewList = context
        visibleItemCount = recyclerView.childCount
        nowTotalItemCount = layoutManager.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (loading && nowTotalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = nowTotalItemCount
        }

        if (!loading && (nowTotalItemCount - visibleItemCount) < (firstVisibleItem + expectedVisibleThreshold)) {
            loading = true
            addNewList?.onAddMovies()
        }
    }

    interface AddNewList {
        fun onAddMovies()
    }

}