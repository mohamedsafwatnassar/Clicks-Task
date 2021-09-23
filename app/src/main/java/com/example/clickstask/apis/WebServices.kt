package com.example.clickstask.apis

import com.example.clickstask.ui.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {

    @GET("top-headlines")
    suspend fun getNewsSource(
        @Query("country") country: String?,
        @Query("apiKey") apiKey: String?
    ): Response<NewsResponse>

}