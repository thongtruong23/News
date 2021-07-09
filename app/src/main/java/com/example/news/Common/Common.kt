package com.example.news.Common

import com.example.news.Interface.NewsService
import com.example.news.Remote.RetrofitClient
import java.lang.StringBuilder

object Common {
    val BASE_URL = "https://newsapi.org"
    val API_KEY = "d95e3496aa8c4c5a8b20401f6232ac24"

    val newsService: NewsService
        get() = RetrofitClient.getClient(BASE_URL).create(NewsService::class.java)

    fun getNewsAPI(source: String): String {
        val apiUrl = StringBuilder("https://newsapi.org/v2/top-headlines?sources=")
            .append(source)
            .append("&apiKey")
            .append(API_KEY)
            .toString()
        return apiUrl
    }

}