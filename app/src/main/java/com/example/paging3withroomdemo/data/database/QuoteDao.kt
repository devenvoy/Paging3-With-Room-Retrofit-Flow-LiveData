package com.example.paging3withroomdemo.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paging3withroomdemo.data.model.Result


@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuotes(quotes: List<Result>)

    @Query("SELECT * FROM quote")
    fun getQuotes(): PagingSource<Int, Result>

    @Query("Delete from quote")
    suspend fun deleteQuotes()
}