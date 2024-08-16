package com.kunal.locoassignment.model

import com.kunal.locoassignment.network.ApiService
import com.kunal.locoassignment.network.RetrofitInstance

class SearchRepository {
    private val apiService: ApiService = RetrofitInstance.retrofit.create(ApiService::class.java)

    suspend fun getResult(
        searchQuery: String,
        pageNo: Int
    ) = apiService.getResult(searchQuery, pageNo)
}