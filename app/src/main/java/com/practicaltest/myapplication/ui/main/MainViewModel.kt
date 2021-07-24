package com.practicaltest.myapplication.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.practicaltest.myapplication.data.repository.NewsRepository

class MainViewModel @ViewModelInject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    val newsDataSource = repository.getNews()
}