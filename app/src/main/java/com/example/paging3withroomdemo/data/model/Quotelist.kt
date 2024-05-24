package com.example.paging3withroomdemo.data.model


import com.google.gson.annotations.SerializedName

data class Quotelist(
    @SerializedName("count")
    val count: Int,
    @SerializedName("lastItemIndex")
    val lastItemIndex: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val quoteResults: List<QuoteResult>,
    @SerializedName("totalCount")
    val totalCount: Int,
    @SerializedName("totalPages")
    val totalPages: Int,
) {
    constructor() : this(0, 0, 0, listOf(), 0, 0)
}