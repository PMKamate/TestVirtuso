package com.practicaltest.myapplication.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.practicaltest.myapplication.data.local.AppDatabase
import com.practicaltest.myapplication.data.local.NewsDao
import com.practicaltest.myapplication.data.remote.NewsRemoteDataSource
import com.practicaltest.myapplication.data.remote.NewsService
import com.practicaltest.myapplication.data.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    @Named("retrofit_1")
    fun provideRetrofit1(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/v2/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideNewsService(@Named("retrofit_1")retrofit2: Retrofit): NewsService = retrofit2.create(NewsService::class.java)

    @Singleton
    @Provides
    fun provideNewsRemoteDataSource(newsService: NewsService) = NewsRemoteDataSource(newsService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)


    @Singleton
    @Provides
    fun provideNewsDao(db: AppDatabase) = db.newsDao()


    @Singleton
    @Provides
    fun provideNewsRepository(remoteDataSource: NewsRemoteDataSource,
                          localDataSource: NewsDao) =
        NewsRepository(remoteDataSource, localDataSource)
}