package com.practicaltest.myapplication.data.remote

import com.practicaltest.myapplication.utils.AppConstant
import com.practicaltest.myapplication.utils.Utils
import javax.inject.Inject

class NewsRemoteDataSource @Inject constructor(
    private val newsService: NewsService
): BaseDataSource() {

    //suspend fun getAllNews() = getResult { newsService.getAllNews(Utils.getCountry(),AppConstant.NEWS_API_KEY) }
    suspend fun getAllNews() = getResult { newsService.getAllNews("us",AppConstant.NEWS_API_KEY) }
}