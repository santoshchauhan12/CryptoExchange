package com.android.cryptocoin.repository

import android.util.Log
import com.android.cryptocoin.model.bitcoin.CryptoData
import com.android.cryptocoin.model.bitcoin.logo.MetaDataResponse
import com.android.cryptocoin.network.ApiInterface
import com.android.cryptocoin.util.AppResult
import com.android.cryptocoin.util.Utils.handleApiError
import com.android.cryptocoin.util.Utils.handleSuccess
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class ExchangeRepository {
    private var interceptor =  HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private var client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(HeaderInterceptor())
        .build()
    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val originalRequest = chain.request()
            val modifiedRequest = originalRequest.newBuilder()
                .header("Content-Type", "application/json")  // Example header
                .header("X-CMC_PRO_API_KEY", "3a54e6b5-676c-4575-8f81-9527e73de6af")  // Example authorization header
                .build()

            return chain.proceed(modifiedRequest)
        }
    }

    var retrofit = Retrofit.Builder()
        .baseUrl("https://pro-api.coinmarketcap.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client)
        .build()

    var service: ApiInterface = retrofit.create(ApiInterface::class.java)


     suspend fun getCryptoList(): AppResult<CryptoData> {

        var response = service.getBitCoinList()
        if (service.getBitCoinList().isSuccessful) {

            Log.e("cryptocurr", " inside success ")

            return handleSuccess(response)
        } else {
            Log.e("cryptocurr", " inside error ")

            return handleApiError(response)
        }
    }

    suspend fun getLogoUrlList(ids: String): AppResult<MetaDataResponse> {

        val response = service.getMetaDataList(ids)
        return if (response.isSuccessful) {

            Log.e("cryptocurr", " inside success ")

            handleSuccess(response)
        } else {
            Log.e("cryptocurr", " inside error ")
            handleApiError(response)
        }
    }
}