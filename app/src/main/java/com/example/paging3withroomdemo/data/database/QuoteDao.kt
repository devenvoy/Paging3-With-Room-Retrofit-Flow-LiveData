package com.example.paging3withroomdemo.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paging3withroomdemo.data.model.QuoteResult


@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuotes(quotes: List<QuoteResult>)

    @Query("SELECT * FROM quote")
    fun getQuotes(): PagingSource<Int, QuoteResult>

    @Query("Delete from quote")
    suspend fun deleteQuotes()
}