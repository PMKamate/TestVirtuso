package com.practicaltest.myapplication.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "newsItem")
data class News(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String?,
    val description: String?,
    val url:String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val author: String?,
)