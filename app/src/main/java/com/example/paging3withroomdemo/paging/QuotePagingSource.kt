package com.example.paging3withroomdemo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3withroomdemo.data.model.QuoteResult
import com.example.paging3withroomdemo.data.retrofit.QuoteApi


// Int refers to page number // Result is model class for response
// we are loading data from api so we provide reference for it.
class QuotePagingSource(private val quoteApi: QuoteApi) : PagingSource<Int, QuoteResult>() {

 // logic to load data - how to load page
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, QuoteResult> {
        return try {
            // params.key will store current page number
            // if current key null then refer to 1
            val position = params.key ?: 1
            val response = quoteApi.getQuotes(position)

             LoadResult.Page<Int, QuoteResult>(
                data = response.quoteResults,    // List<Result>
                // if position on first page then no prev key
                prevKey = if (position == 1) null else position - 1,
                // if position on last page then no next key
                nextKey = if (position == response.totalPages) null else position + 1
            )
        } catch (e: Exception) {
            // if something went wrong then we pass error
            LoadResult.Error(e)
        }
    }

    // if pagingSource lost current page key it will help to find next key to load data from
    override fun getRefreshKey(state: PagingState<Int, QuoteResult>): Int? {
// anchorPosition hold value for last page visited but it can be null so we use null check if not null then we proceed
        return state.anchorPosition?.let { anchorPosition ->
// it will find closest page from last page encountered
            val anchorPage = state.closestPageToPosition(anchorPosition)

            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}