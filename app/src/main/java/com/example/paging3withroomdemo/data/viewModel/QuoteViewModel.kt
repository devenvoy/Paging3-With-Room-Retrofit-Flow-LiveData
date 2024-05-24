package com.example.paging3withroomdemo.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.example.paging3withroomdemo.data.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository,
) : ViewModel() {
    // it will return live data that we will store here
    // in paging we need to cache our data for that it has one method.
    // it can cache our Livedata of PagingData inside cache memory using coroutine scope
    // benefit : performance
    val list = quoteRepository.getQuotes().cachedIn(viewModelScope)
}