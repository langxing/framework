package com.chaomeng.utils

import android.content.Context
import android.net.ConnectivityManager
import com.chaomeng.HttpModule

class NetworkManager {

    companion object {
        private val instance: NetworkManager by lazy {
            NetworkManager()
        }

        fun get() = instance
    }

    fun checkNetwork(): Boolean {
        val connectivityManager = HttpModule.newInstance.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}