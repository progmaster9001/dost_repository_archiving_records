package com.ojtapp.mobile

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RarApp: Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
    }
}