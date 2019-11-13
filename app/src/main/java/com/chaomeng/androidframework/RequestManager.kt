package com.chaomeng.androidframework

import com.chaomeng.http.OkHttpRequest
import com.chaomeng.http.RequestFactroy

class RequestManager private constructor(): RequestFactroy<OkHttpRequest> {

    companion object {
        private val instance: RequestManager by lazy {
            RequestManager()
        }

        fun get() = instance
    }

    override fun getRequest(): OkHttpRequest {
        return OkHttpRequest.get()
    }

}