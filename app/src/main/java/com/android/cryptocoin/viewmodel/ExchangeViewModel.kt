package com.android.cryptocoin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.cryptocoin.model.bitcoin.CryptoData
import com.android.cryptocoin.model.bitcoin.CryptoItem
import com.android.cryptocoin.model.bitcoin.logo.MetaDataResponse
import com.android.cryptocoin.repository.ExchangeRepository
import com.android.cryptocoin.util.AppResult
import com.android.cryptocoin.util.NetworkManager.isOnline
import com.android.cryptocoin.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExchangeViewModel: ViewModel(), KoinComponent {

    private val exchangeRepository : ExchangeRepository by inject()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()
    private var cryptoListJob : Deferred<AppResult<CryptoData>> ?= null
    private var logoUrlJob : Deferred<AppResult<MetaDataResponse>> ?= null
    var bitCoinListLiveData = MutableLiveData<List<CryptoItem>>()
    val showError = SingleLiveEvent<String?>()


    suspend fun getBitCoinList() {

        if(isOnline()) {
            loading.value = true
            cryptoListJob = CoroutineScope(Dispatchers.IO + exceptionHandler).async {
                getCurrencyList()
            }

            val cryptoListResult = cryptoListJob?.await()

            val idList: List<String>
            var idToLogoUrlMap: Map<String, String>? = null


            when (cryptoListResult) {
                is AppResult.Success -> {
                    idList = cryptoListResult.successData.data.map { it.id }
                    logoUrlJob = CoroutineScope(Dispatchers.IO + exceptionHandler).async {
                        getLogoUrlList(idList)
                    }


                    when (val logoUrlListResult = logoUrlJob?.await()) {

                        is AppResult.Success -> {

                            idToLogoUrlMap = createIdToLogoUrlMap(logoUrlListResult.successData)
                            loading.value = false
                        }

                        is AppResult.Error -> {
                            loading.value = false
                            if(logoUrlListResult.message.isNullOrEmpty()) {
                                showError.postValue(logoUrlListResult.exception.message)
                            } else {
                                showError.postValue(logoUrlListResult.message)
                            }
                        }

                        else -> {}
                    }
                    cryptoListResult.successData.data.forEach { item1 ->
                        val logoUrl = idToLogoUrlMap?.get(item1.id)
                        logoUrl?.let { item1.logoUrl = it }
                    }
                    bitCoinListLiveData.postValue(cryptoListResult.successData.data)
                }

                is AppResult.Error -> {
                    if(cryptoListResult.message.isNullOrEmpty()) {
                        showError.postValue(cryptoListResult.exception.message)
                    } else {
                        showError.postValue(cryptoListResult.message)
                    }
                    loading.value = false
                }

                else -> {}
            }
        } else {
            showError.postValue("check your network connection")
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
        return exchangeRepository.getCryptoList()
    }

    private suspend fun getLogoUrlList(ids: List<String>) : AppResult<MetaDataResponse> {
        return exchangeRepository.getLogoUrlList(ids.joinToString(","))
    }

    private fun onError(message: String) {
        showError.postValue(message)
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        cryptoListJob?.cancel()

        logoUrlJob?.cancel()
    }
}