package com.example.examplecodingkotlin.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by fbarbieri on 2019-08-29.
 */

abstract class EndlessRecyclerViewScrollListener protected constructor(layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    private val visibleThreshold = 10
    private var currentPage = 1
    private var previousTotalItemCount = 0
    private var loading = true
    private val startingPageIndex = 1
    private val mLayoutManager: RecyclerView.LayoutManager

    init {
        mLayoutManager = layoutManager
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        val lastVisibleItemPosition: Int
        val totalItemCount = mLayoutManager.itemCount

        lastVisibleItemPosition = (mLayoutManager as LinearLayoutManager)
                .findLastVisibleItemPosition()

        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, view)
            loading = true
        }
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int, recyclerView: RecyclerView)
}