package com.practicaltest.myapplication.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.practicaltest.myapplication.data.entities.News
import com.practicaltest.myapplication.data.repository.NewsRepository
import com.practicaltest.myapplication.utils.Constants

class MainViewModel @ViewModelInject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    val newsDataSource = repository.getNews()

    private val config = PagedList.Config.Builder()
        .setPageSize(Constants.PAGE_SIZE)
        .setInitialLoadSizeHint(Constants.PAGE_INITIAL_LOAD_SIZE_HINT)
        .setPrefetchDistance(Constants.PAGE_PREFETCH_DISTANCE)
        .setEnablePlaceholders(false)
        .build()

    fun getPagedNewsListViewModel(): LiveData<PagedList<News>> {
        return repository.getPagedList(config)
    }
}