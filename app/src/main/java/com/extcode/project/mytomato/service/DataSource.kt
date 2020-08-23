package com.extcode.project.mytomato.service

import com.extcode.project.mytomato.data.response.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DataSource {

    @GET("top-headlines?country=id")
    fun userNews(
        @Query("category") category: String?
    ): Call<NewsResponse>

}