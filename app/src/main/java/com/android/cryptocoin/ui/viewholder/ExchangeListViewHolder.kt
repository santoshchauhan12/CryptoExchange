package com.android.cryptocoin.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.android.cryptocoin.R
import com.android.cryptocoin.databinding.ItemViewCurrencyBinding
import com.android.cryptocoin.model.bitcoin.CryptoItem
import com.android.cryptocoin.util.Utils
import com.bumptech.glide.Glide

class ExchangeListViewHolder(private val binding: ItemViewCurrencyBinding): RecyclerView.ViewHolder(binding.root) {


    fun bind(cryptoItem: CryptoItem) {
        binding.tvName.text = cryptoItem.name
        binding.tvSymbol.text = cryptoItem.symbol
        binding.tvPercentStatus.text = Utils.appendPercentUsingInterpolation(Utils.formatDoubleToOneDecimal(
            cryptoItem.quote.USD.volumeChange24h))

        if(cryptoItem.quote.USD.volumeChange24h < 0) {
            Utils.setBgViewColor(binding.ivChangeGraph, R.drawable.ic_negative_graph)
            binding.tvPercentStatus.setTextColor(binding.root.context.resources.getColor(R.color.red))
            binding.tvPercentStatus.text = Utils.appendPercentUsingInterpolation(Utils.formatDoubleToOneDecimal(
                cryptoItem.quote.USD.volumeChange24h))
        } else {
            Utils.setBgViewColor(binding.ivChangeGraph, R.drawable.ic_positive_graph)
            binding.tvPercentStatus.setTextColor(binding.root.context.resources.getColor(R.color.green))
            binding.tvPercentStatus.text = "+ ".plus(Utils.appendPercentUsingInterpolation(Utils.formatDoubleToOneDecimal(
                cryptoItem.quote.USD.volumeChange24h)))

        }

        Glide.with(binding.root.context).load(cryptoItem.logoUrl).into(binding.ivTopCurrencyLogo)
        binding.tvPrice.text = Utils.prependDollarUsingConcatenation(cryptoItem.quote.USD.price.toInt().toString()).plus(" USD")
    }

}