package com.example.paging3withroomdemo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.paging3withroomdemo.data.model.QuoteRemoteKey
import com.example.paging3withroomdemo.data.model.QuoteResult
import com.example.paging3withroomdemo.utils.Converters

@TypeConverters(Converters::class)
@Database(entities = [QuoteResult::class, QuoteRemoteKey::class], version = 1)
abstract class QuoteDatabase : RoomDatabase() {

    abstract fun getQuoteDao(): QuoteDao
    abstract fun getRemoteKeyDao(): RemoteKeyDao

}