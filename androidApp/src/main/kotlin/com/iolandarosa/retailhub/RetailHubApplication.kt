/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub

import android.app.Application
import com.iolandarosa.retailhub.composeapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class RetailHubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@RetailHubApplication)
        }
    }
}
