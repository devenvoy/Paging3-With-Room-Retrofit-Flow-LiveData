package com.example.paging3withroomdemo.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.paging3withroomdemo.data.retrofit.QuoteApi
import com.example.paging3withroomdemo.paging.QuotePagingSource
import javax.inject.Inject


// Pager is second component for paging
// here we will define Pager
class QuoteRepository @Inject constructor(val quoteApi: QuoteApi) {

    // we need repository to provide data
    // we will get data from paging Source
    // pager will interact with paging source

    fun getQuotes()  = Pager(
        // one page has 20 records and it will hold maximum 100 records in memory then it will drop old
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { QuotePagingSource(quoteApi) }
    ).liveData

    // to expose our data we have .livedata  or .flow
    // so it will return data in form of Livedata or Flow

}