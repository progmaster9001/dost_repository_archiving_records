package com.ojtapp.mobile

import android.app.Application
import com.ojtapp.mobile.data.ServiceLocator

class RarApp: Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
    }
}