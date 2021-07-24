package com.practicaltest.myapplication.data.remote

import com.practicaltest.myapplication.model.news.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("top-headlines")
    suspend fun getAllNews(
        @Query("country") country: String?,
        @Query("apiKey") apiKey: String?
    ):Response<News>

}