package com.example.paging3withroomdemo.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paging3withroomdemo.data.model.QuoteRemoteKey
import com.example.paging3withroomdemo.data.model.Result

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(remoteKeys: List<QuoteRemoteKey>)

    @Query("SELECT * FROM quote_remote_key where id = :id")
    fun getRemoteKeys(id: String): QuoteRemoteKey

    @Query("Delete from quote_remote_key")
    suspend fun deleteRemoteKeys()
}