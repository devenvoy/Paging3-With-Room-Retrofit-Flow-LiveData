package com.example.paging3withroomdemo.paging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3withroomdemo.data.model.QuoteResult
import com.example.paging3withroomdemo.databinding.ItemQuoteLayoutBinding


// we will create adapter like normal RecyclerView Adapter but here it will be  PagingDataAdapter<>()
// our adapter will hold Comparator using constructor
// we will define type of data objects and ViewHolder inside this class type
class QuotePagingAdapter :
    PagingDataAdapter<QuoteResult, QuotePagingAdapter.QuoteViewHolder>(diffCallback = Comparator()) {

    class QuoteViewHolder(private val binding: ItemQuoteLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: QuoteResult) {
            binding.apply {
                quoteTv.text = item.content
            }
        }
    }

    override fun onBindViewHolder(
        holder: QuoteViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val newItem = payloads[0] as QuoteResult
            holder.onBind(newItem)
        }
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        // getItem() will provide current data object based on position
        holder.onBind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(
            ItemQuoteLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class Comparator : DiffUtil.ItemCallback<QuoteResult>() {
        override fun areItemsTheSame(oldItem: QuoteResult, newItem: QuoteResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: QuoteResult, newItem: QuoteResult): Boolean {
            return oldItem == newItem
        }
    }

}