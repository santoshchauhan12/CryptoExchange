package com.android.cryptocoin.model.bitcoin

import com.google.gson.annotations.SerializedName

data class CryptoItem(
    val id: String,
    val name: String,
    val symbol: String,
    val slug: String,
    @SerializedName("cmc_rank") val cmcRank: Double,
    @SerializedName("num_market_pairs") val numMarketPairs: Double,
    @SerializedName("circulating_supply") val circulatingSupply: Double,
    @SerializedName("total_supply") val totalSupply: Double,
    @SerializedName("max_supply") val maxSupply: Double,
    @SerializedName("infinite_supply") val infiniteSupply: Boolean,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("date_added") val dateAdded: String,
    val tags: List<String>,
    val platform: Any?,  // You may replace "Any?" with the actual type if platform has a specific structure
    @SerializedName("self_reported_circulating_supply") val selfReportedCirculatingSupply: Any?,
    @SerializedName("self_reported_market_cap") val selfReportedMarketCap: Any?,
    val quote: Quote,
    var logoUrl: String
)