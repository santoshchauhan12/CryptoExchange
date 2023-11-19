package com.android.cryptocoin.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.cryptocoin.databinding.ItemViewCurrencyBinding
import com.android.cryptocoin.model.bitcoin.CryptoItem
import com.android.cryptocoin.ui.viewholder.ExchangeListViewHolder

class ExchangeListAdapter(shoppingList: List<CryptoItem>?): RecyclerView.Adapter<ExchangeListViewHolder>() {

    var shopList: List<CryptoItem>?= null
    var filteredList: List<CryptoItem>?= null

    init {
        shopList = shoppingList
        filteredList = shoppingList
    }

    fun updateList(newList: MutableList<CryptoItem>) {
        filteredList = newList
        notifyDataSetChanged()
    }

    fun filter(query: String) {

        filteredList = if (query.isEmpty()) {
            shopList
        } else {
            shopList?.filter {
                it.name.contains(query, true) || it.symbol.contains(query, true)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemViewCurrencyBinding.inflate(inflater, parent, false)
        return ExchangeListViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return filteredList?.size!!
    }

    override fun onBindViewHolder(holder: ExchangeListViewHolder, position: Int) {

        filteredList?.let {
            holder.bind(it[position])
        }

    }
}