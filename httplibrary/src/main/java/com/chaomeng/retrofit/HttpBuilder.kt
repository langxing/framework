package com.chaomeng.retrofit

import com.chaomeng.http.SSLSocketManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager
import kotlin.collections.HashMap

class HttpBuilder {

    val globalHeaders = IdentityHashMap<String, Any>()
    var globalParams = HashMap<String, Any>()
    private var httpBuilder = OkHttpClient.Builder()

    companion object {
        private val instance: HttpBuilder by lazy {
            HttpBuilder()
        }

        fun get() = instance
    }

    fun connectTimeout(time: Long = 30, timeUnit: TimeUnit = TimeUnit.SECONDS): HttpBuilder {
        httpBuilder.connectTimeout(time, timeUnit)
        return this
    }

    fun readTimeout(time: Long = 30, timeUnit: TimeUnit = TimeUnit.SECONDS): HttpBuilder {
        httpBuilder.readTimeout(time, timeUnit)
        return this
    }

    fun writeTimeout(time: Long = 30, timeUnit: TimeUnit = TimeUnit.SECONDS): HttpBuilder {
        httpBuilder.writeTimeout(time, timeUnit)
        return this
    }

    fun setSSLFactory(sslSocketFactory: SSLSocketFactory? = null, trustManager: X509TrustManager? = null): HttpBuilder {
        val sslFactory = sslSocketFactory ?: SSLSocketManager.get().defaultSSLSocketFactory()
        val manager = trustManager ?: SSLSocketManager.get().defaultTrustManager()
        sslFactory?.let {
            httpBuilder.sslSocketFactory(sslFactory, manager)
        }
        return this
    }

    fun setHostnameVerifier(hostnameVerifier: HostnameVerifier? = null): HttpBuilder {
        val verifier = hostnameVerifier ?: SSLSocketManager.TrustAllHostnameVerifier()
        httpBuilder.hostnameVerifier(verifier)
        return this
    }

    fun addInterceptor(interceptor: Interceptor): HttpBuilder {
        httpBuilder.addInterceptor(interceptor)
        return this
    }

    fun addParams(params: Pair<String, Any>): HttpBuilder {
        globalParams[params.first] = params.second
        return this
    }

    fun addHeader(header: Pair<String, Any>): HttpBuilder {
        globalHeaders[header.first] = header.second
        return this
    }

    fun build(): OkHttpClient {
        httpBuilder.addInterceptor(UrlInterceptor())
        httpBuilder.addInterceptor(GlobalInterceptor())
        return httpBuilder.build()
    }
}