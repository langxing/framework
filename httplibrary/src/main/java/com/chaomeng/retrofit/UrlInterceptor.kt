package com.chaomeng.retrofit

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class UrlInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!BaseUrl.get().useDefaultUrl && BaseUrl.get().baseUrl.isNotEmpty()) {
            val builder = request.newBuilder()
            val oldHttpUrl = request.url()
            val newUrl = HttpUrl.parse(BaseUrl.get().baseUrl)
            BaseUrl.get().useDefaultUrl = true
            newUrl?.let {
                val newHttpUrl = oldHttpUrl.newBuilder()
                    .scheme(it.scheme())
                    .host(it.host())
                    .port(it.port())
                    .build()
                val newRequest = builder.url(newHttpUrl).build()
                return chain.proceed(newRequest)
            }
        }
        return chain.proceed(request)
    }
}