package com.kunal.locoassignment.model

import com.google.gson.annotations.SerializedName
import kotlin.random.Random

data class SearchResultResponse(
    @SerializedName("Search")
    val search: List<ResultDetail>?,
    @SerializedName("totalResults")
    val totalResults: String?,
    @SerializedName("Response")
    val response: String?
)

data class ResultDetail(
    @SerializedName("Title")
    val title: String,
    @SerializedName("Year")
    val year: String,
    @SerializedName("imdbID")
    val imdbID: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Poster")
    val poster: String,
    var rating : Int = 0
)