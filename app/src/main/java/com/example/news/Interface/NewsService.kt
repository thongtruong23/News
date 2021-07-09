package com.example.news.Interface

import com.example.news.Model.News
import com.example.news.Model.Website
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


interface NewsService {


    //Delete https://newsapi.org because it is BASE_URL in Common file
    @get:GET("v2/top-headlines/sources?apiKey=d95e3496aa8c4c5a8b20401f6232ac24")
    val sources: Call<Website>

    @GET
    fun getNewsFromSource(@Url url : String): Call<News>
}