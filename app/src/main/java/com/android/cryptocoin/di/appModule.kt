package com.android.cryptocoin.model.bitcoin.di

import com.android.cryptocoin.repository.ExchangeRepository
import org.koin.dsl.module

val appModule = module {
    // ViewModel
//    viewModel { MyViewModel(get()) }

    // Repository
    single { ExchangeRepository() }
}