package com.practicaltest.myapplication.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.practicaltest.myapplication.data.entities.News
import com.practicaltest.myapplication.data.local.NewsDao
import com.practicaltest.myapplication.data.remote.NewsRemoteDataSource
import com.practicaltest.myapplication.model.news.Article
import com.practicaltest.myapplication.utils.performGetOperation
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val localNewsDataSource: NewsDao
) {

    fun getPagedList(config: PagedList.Config): LiveData<PagedList<News>> {
        val factory: DataSource.Factory<Int, News> = localNewsDataSource.getNewsListPaged()
        return LivePagedListBuilder(factory, config)
            .build()
    }
    fun getNews() = performGetOperation(
        databaseQuery = { localNewsDataSource.getAllNews() },
        networkCall = { newsRemoteDataSource.getAllNews() },
        saveCallResult = { localNewsDataSource.insertAll(getNewsList(it.article)) }
    )

    fun getNewsList(itemResponse: List<Article>?): List<News> {
        Log.d("Test: ",""+itemResponse?.size)
        val newsList = ArrayList<News>()
        itemResponse?.forEach { model ->
            newsList.add(
                News(
                    0,
                    model.title,
                    model.description,
                    model.url,
                    model.urlToImage,
                    model.publishedAt.toString(),
                    model.author
                )
            )
        }
        return newsList

    }

}