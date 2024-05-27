package com.example.paging3withroomdemo.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.paging3withroomdemo.data.database.QuoteDatabase
import com.example.paging3withroomdemo.data.model.QuoteRemoteKey
import com.example.paging3withroomdemo.data.model.QuoteResult
import com.example.paging3withroomdemo.data.retrofit.QuoteApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
class QuoteRemoteMediater @Inject
constructor(
    private val quoteApi: QuoteApi,
    private val quoteDatabase: QuoteDatabase,
) : RemoteMediator<Int, QuoteResult>() {

    private val quoteDao = quoteDatabase.getQuoteDao()
    private val remoteKeyDao = quoteDatabase.getRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, QuoteResult>,
    ): MediatorResult {

        // step 1 fetch quotes from api
        // step 2 save these quotes + remoteKeys data into database
        // step 3 logic for state - REFRESH , PREPEND , APPEND

        return try {

            val currentPage = when (loadType) {
                // Refresh -> when first load or data invalidate
                LoadType.REFRESH -> {
                    val remoteKeys = CoroutineScope(Dispatchers.IO).async {
                        getRemoteKeyClosestToCurrentPosition(state)
                    }.await()
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                //  Prepend -> when scroll up
                LoadType.PREPEND -> {
                    val remoteKeys = CoroutineScope(Dispatchers.IO).async {
                        getRemoteKeyForFirstItem(state)
                    }.await()
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }

                // Append -> When Scroll Down
                LoadType.APPEND -> {
                    val remoteKeys = CoroutineScope(Dispatchers.IO).async {
                        getRemoteKeyForLastItem(state)
                    }.await()
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }

            }

            // hit api
            val response = quoteApi.getQuotes(currentPage)
            // is last page == true
            val endOfPaginationReached = response.totalPages == currentPage

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            quoteDatabase.withTransaction {

                // if refresh then first clear data from database to load new data
                if (loadType == LoadType.REFRESH) {
                    quoteDao.deleteQuotes()
                    remoteKeyDao.deleteRemoteKeys()
                }

                // store response in room database
                quoteDao.addQuotes(response.quoteResults)

                val keys = response.quoteResults.map { quote ->
                    QuoteRemoteKey(
                        id = quote.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }

                remoteKeyDao.insertRemoteKey(remoteKeys = keys)
            }

            MediatorResult.Success(endOfPaginationReached)

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, QuoteResult>,
    ): QuoteRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeyDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, QuoteResult>,
    ): QuoteRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { quote ->
                remoteKeyDao.getRemoteKeys(id = quote.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, QuoteResult>,
    ): QuoteRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { quote ->
                remoteKeyDao.getRemoteKeys(id = quote.id)
            }
    }
}