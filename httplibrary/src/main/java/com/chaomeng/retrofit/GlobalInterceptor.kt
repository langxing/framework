package com.chaomeng.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class GlobalInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        val url = request.url()
        val builder = url.newBuilder()
            .scheme(url.scheme())
            .host(url.host())
            .port(url.port())
        HttpBuilder.get().globalParams.forEach {
            builder.addQueryParameter(it.key, "${it.value}")
        }
        HttpBuilder.get().globalHeaders.forEach {
            requestBuilder.addHeader(it.key, "${it.value}")
        }
        val newUrl = builder.build()
        val newRequest = requestBuilder
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }
}