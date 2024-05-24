package com.example.paging3withroomdemo.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.Loader
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paging3withroomdemo.data.viewModel.QuoteViewModel
import com.example.paging3withroomdemo.databinding.ActivityMainBinding
import com.example.paging3withroomdemo.paging.adapter.LoaderAdpater
import com.example.paging3withroomdemo.paging.adapter.QuotePagingAdapter
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val myViewModel: QuoteViewModel by viewModels()

    private lateinit var quoteAdapter: QuotePagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quoteAdapter = QuotePagingAdapter()

        binding.apply {

            quotesRv.apply {
                layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                adapter = quoteAdapter.withLoadStateHeaderAndFooter(
                    header = LoaderAdpater(),
                    footer = LoaderAdpater()
                )
                setHasFixedSize(true)
            }
        }

        myViewModel.list.observe(this) {
            // here we need to pass lifecycle
            quoteAdapter.submitData(lifecycle = lifecycle, pagingData = it)
        }


    }
}