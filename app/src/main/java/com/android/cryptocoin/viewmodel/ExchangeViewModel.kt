package com.android.cryptocoin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.cryptocoin.model.bitcoin.CryptoData
import com.android.cryptocoin.model.bitcoin.CryptoItem
import com.android.cryptocoin.model.bitcoin.logo.MetaDataResponse
import com.android.cryptocoin.repository.ExchangeRepository
import com.android.cryptocoin.util.AppResult
import com.android.cryptocoin.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExchangeViewModel(): ViewModel(), KoinComponent {

    val exchangeRepository : ExchangeRepository by inject()
    val errorMessage = MutableLiveData<String>()

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    var cryptoListJob : Deferred<AppResult<CryptoData>> ?= null
    var logoUrlJob : Deferred<AppResult<MetaDataResponse>> ?= null

    var bitCoinListLiveData = MutableLiveData<List<CryptoItem>>()
//    val showLoading = ObservableBoolean()
    val showError = SingleLiveEvent<String>()


    suspend fun getBitCoinList() {
         cryptoListJob = CoroutineScope(Dispatchers.IO + exceptionHandler).async {
             getCurrencyList()
         }


        val firstApiResponse = cryptoListJob?.await()



        val idList: List<String>
        var idToLogoUrlMap : Map<String, String> ?= null


        when(firstApiResponse) {
            is AppResult.Success -> {
                idList = firstApiResponse.successData.data.map { it.id }
                logoUrlJob = CoroutineScope(Dispatchers.IO + exceptionHandler).async {
                    getLogoUrlList(idList)
                }


                when(val secondApiResponse = logoUrlJob?.await()) {

                    is AppResult.Success -> {

                        idToLogoUrlMap = createIdToLogoUrlMap(secondApiResponse.successData)
                    }

                    is AppResult.Error -> {

                    }

                    else -> {}
                }
                firstApiResponse.successData.data.forEach { item1 ->
                    val logoUrl = idToLogoUrlMap?.get(item1.id)
                    logoUrl?.let { item1.logoUrl = it }
                }
                bitCoinListLiveData.postValue(firstApiResponse.successData.data)
            }

            is AppResult.Error -> {

            }

            else -> {}
        }




    }


    private suspend fun createIdToLogoUrlMap(apiResponse2: MetaDataResponse): Map<String, String> {
        // Create a map of id to logoUrl from the second response
        return withContext(Dispatchers.Default) {
            val map = HashMap<String, String>()
            for (item2 in apiResponse2.data) {
                map[item2.key] = item2.value.logo
            }
            map
        }
    }


    private suspend fun getCurrencyList() : AppResult<CryptoData> {
        val response = exchangeRepository.getCryptoList()
        return response
    }

    private suspend fun getLogoUrlList(ids: List<String>) : AppResult<MetaDataResponse> {
        val response = exchangeRepository.getLogoUrlList(ids.joinToString(","))
        return response

    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
//        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        cryptoListJob?.cancel()

        logoUrlJob?.cancel()
    }
}