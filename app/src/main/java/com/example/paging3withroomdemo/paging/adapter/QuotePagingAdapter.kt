package com.example.paging3withroomdemo.paging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3withroomdemo.data.model.Result
import com.example.paging3withroomdemo.databinding.ItemQuoteLayoutBinding


// we will create adapter like normal RecyclerView Adapter but here it will be  PagingDataAdapter<>()
// our adapter will hold Comparator using constructor
// we will define type of data objects and ViewHolder inside this class type
class QuotePagingAdapter :
    PagingDataAdapter<Result, QuotePagingAdapter.QuoteViewHolder>(diffCallback = Comparator()) {

    class QuoteViewHolder(private val binding: ItemQuoteLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Result) {
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
            val newItem = payloads[0] as Result
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

    class Comparator : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

}