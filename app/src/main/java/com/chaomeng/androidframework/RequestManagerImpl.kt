package com.chaomeng.androidframework

import com.chaomeng.http.OkHttpRequest
import com.chaomeng.http.RequestManager

class RequestManagerImpl private constructor(): RequestManager<OkHttpRequest> {

    companion object {
        private val instance: RequestManagerImpl by lazy {
            RequestManagerImpl()
        }

        fun get() = instance
    }

    override fun createRequest(): OkHttpRequest {
        return OkHttpRequest.get()
    }

}