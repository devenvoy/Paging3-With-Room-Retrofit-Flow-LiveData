package com.example.paging3withroomdemo.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.paging3withroomdemo.data.database.QuoteDao
import com.example.paging3withroomdemo.data.database.QuoteDatabase
import com.example.paging3withroomdemo.data.database.RemoteKeyDao
import com.example.paging3withroomdemo.data.model.QuoteRemoteKey
import com.example.paging3withroomdemo.data.model.Result
import com.example.paging3withroomdemo.data.retrofit.QuoteApi
import javax.inject.Inject

@ExperimentalPagingApi
class QuoteRemoteMediater @Inject
constructor(
    private val quoteApi: QuoteApi,
    private val quoteDatabase: QuoteDatabase,
) : RemoteMediator<Int, Result>() {

    val quoteDao = quoteDatabase.getQuoteDao()
    val remoteKeyDao = quoteDatabase.getRemoteKeyDao()
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {

        // step 1 Hit api
        // step 2 store api response inside database
        // step 3 make remote key table and populate it

        return try {

            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }
            val response = quoteApi.getQuotes(currentPage)
            val endOfPaginationReached = response.totalPages == currentPage

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            quoteDatabase.withTransaction {
                quoteDao.addQuotes(response.results)
                val keys = response.results.map { quote ->
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
        state: PagingState<Int, Result>,
    ): QuoteRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeyDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Result>,
    ): QuoteRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { quote ->
                remoteKeyDao.getRemoteKeys(id = quote.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Result>,
    ): QuoteRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { quote ->
                remoteKeyDao.getRemoteKeys(id = quote.id)
            }
    }
}