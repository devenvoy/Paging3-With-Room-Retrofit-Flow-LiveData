package com.example.paging3withroomdemo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quote_remote_key")
data class QuoteRemoteKey(

    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?,
) {
    constructor() : this("", 0, 0)
}
