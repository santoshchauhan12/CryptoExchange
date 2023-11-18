package com.android.cryptocoin

import android.app.Application
import android.content.Context
import com.android.cryptocoin.model.bitcoin.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CryptoCoinApp : Application() {


    companion object {
        var context : Context ?= null
    }
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CryptoCoinApp)
            modules(appModule)
        }

        com.android.cryptocoin.CryptoCoinApp.Companion.context = this

    }
}