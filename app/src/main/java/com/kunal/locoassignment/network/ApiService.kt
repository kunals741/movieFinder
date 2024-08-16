package com.kunal.locoassignment.network

import com.kunal.locoassignment.BuildConfig
import com.kunal.locoassignment.model.SearchResultResponse
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @GET("/?${NetworkConstants.API_KEY}=${BuildConfig.API_KEY}")
    suspend fun getResult(
        @Query(NetworkConstants.SEARCH) search: String,
        @Query(NetworkConstants.PAGE_NO) pageNo: Int,
    ): Response<SearchResultResponse>
}