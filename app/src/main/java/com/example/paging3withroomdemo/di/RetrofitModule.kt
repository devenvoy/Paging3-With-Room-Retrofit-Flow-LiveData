package com.example.paging3withroomdemo.di

import com.example.paging3withroomdemo.data.retrofit.QuoteApi
import com.example.paging3withroomdemo.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providesQuoteApi(retrofit: Retrofit): QuoteApi = retrofit.create(QuoteApi::class.java)

}