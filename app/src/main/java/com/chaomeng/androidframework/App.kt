package com.chaomeng.androidframework

import com.chaomeng.HttpModule
import android.app.Application
import com.chaomeng.androidframework.utils.CacheManager
import com.chaomeng.androidframework.utils.ImageLoader
import com.chaomeng.http.OkHttpRequest
import com.chaomeng.retrofit.HttpBuilder
import com.chaomeng.retrofit.RetrofitManager
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class App : Application() {

    companion object {
        private lateinit var instance: App

        fun getApp(): App {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        CacheManager.get().initCache()
        OkHttpRequest.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addHeader("id" to 2)
            .addParams("token" to "219389ur8tu8yeqw")
            .setSSLFactory()
            .setHostnameVerifier()
            .build()
        val okHttpClient = HttpBuilder.get()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addHeader("id" to 2)
            .build()
        RetrofitManager.Builder()
            .client(okHttpClient)
            .defaultUrl("https://www.apiopen.top")
            .useRxJava()
            .callAdapter()
            .addConverterFactory()
            .build()
        HttpModule.newInstance.init(this)
    }

}