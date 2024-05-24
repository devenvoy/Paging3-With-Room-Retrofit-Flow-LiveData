package com.example.paging3withroomdemo.data.retrofit

import com.example.paging3withroomdemo.data.model.Quotelist
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteApi {

    @GET("/quotes")
    suspend fun getQuotes(@Query("page") page: Int): Quotelist
}