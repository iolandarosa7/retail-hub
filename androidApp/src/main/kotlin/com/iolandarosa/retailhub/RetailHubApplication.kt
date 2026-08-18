package com.iolandarosa.retailhub

import android.app.Application
import com.iolandarosa.retailhub.composeapp.di.initKoin

class RetailHubApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}