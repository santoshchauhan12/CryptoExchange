package com.android.cryptocoin.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.cryptocoin.databinding.ItemViewCurrencyBinding
import com.android.cryptocoin.model.bitcoin.CryptoItem
import com.android.cryptocoin.ui.viewholder.ExchangeListViewHolder

class ExchangeListAdapter(shoppingList: List<CryptoItem>?): RecyclerView.Adapter<ExchangeListViewHolder>() {

    var shopList: List<CryptoItem>?= null

    init {
        shopList = shoppingList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemViewCurrencyBinding.inflate(inflater, parent, false)
        return ExchangeListViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return shopList?.size!!
    }

    override fun onBindViewHolder(holder: ExchangeListViewHolder, position: Int) {

        shopList?.let {
            holder.bind(it[position])
        }

    }
}