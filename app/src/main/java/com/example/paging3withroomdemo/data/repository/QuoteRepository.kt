package com.example.paging3withroomdemo.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.paging3withroomdemo.data.database.QuoteDatabase
import com.example.paging3withroomdemo.data.retrofit.QuoteApi
import com.example.paging3withroomdemo.paging.QuoteRemoteMediater
import javax.inject.Inject


// Pager is second component for paging
// here we will define Pager
@ExperimentalPagingApi
class QuoteRepository @Inject constructor(
    private val quoteApi: QuoteApi,
    private val quoteDatabase: QuoteDatabase,
) {

    // we need repository to provide data
    // we will get data from paging Source
    // pager will interact with paging source

    fun getQuotes() = Pager(
        // one page has 20 records and it will hold maximum 100 records in memory then it will drop old
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100
        ),

        // remote mediator object need to pass to get data for database
        remoteMediator = QuoteRemoteMediater(quoteApi, quoteDatabase),

        // here instead pulling data from api we are pulling from database
        pagingSourceFactory = { quoteDatabase.getQuoteDao().getQuotes() }
    ).liveData

    // to expose our data we have .livedata  or .flow
    // so it will return data in form of Livedata or Flow

}