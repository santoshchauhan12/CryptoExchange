package com.android.cryptocoin.network

import com.android.cryptocoin.model.bitcoin.CryptoData
import com.android.cryptocoin.model.bitcoin.logo.MetaDataResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getBitCoinList(): Response<CryptoData>

    @GET("v2/cryptocurrency/info")
    suspend fun getMetaDataList(@Query("id") ids: String): Response<MetaDataResponse>

}