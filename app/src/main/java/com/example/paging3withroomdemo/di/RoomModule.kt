package com.example.paging3withroomdemo.di

import android.content.Context
import androidx.room.Room
import com.example.paging3withroomdemo.data.database.QuoteDao
import com.example.paging3withroomdemo.data.database.QuoteDatabase
import com.example.paging3withroomdemo.data.database.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Provides
    @Singleton
    fun providesDatabse(@ApplicationContext context: Context): QuoteDatabase =
        Room.databaseBuilder(
            context,
            QuoteDatabase::class.java,
            "quoteDB"
        ).build()

//    @Provides
//    @Singleton
//    fun providesQuoteDao(quoteDatabase: QuoteDatabase): QuoteDao = quoteDatabase.getQuoteDao()
//
//    @Provides
//    @Singleton
//    fun providesRemoteKeyDao(quoteDatabase: QuoteDatabase): RemoteKeyDao =
//        quoteDatabase.getRemoteKeyDao()

}