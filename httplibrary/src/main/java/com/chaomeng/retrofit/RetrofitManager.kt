package com.chaomeng.retrofit

import com.chaomeng.jsondeserializer.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class RetrofitManager {

    private lateinit var retrofit: Retrofit

    companion object {
        private val instance: RetrofitManager by lazy {
            RetrofitManager()
        }

        fun get() = instance
    }

    class Builder {

        private var manager = get()
        private var retrofitBuilder = Retrofit.Builder()

        fun client(client: OkHttpClient): Builder {
            retrofitBuilder.client(client)
            return this
        }

        fun defaultUrl(url: String): Builder {
            BaseUrl.get().defaultUrl = url
            retrofitBuilder.baseUrl(url)
            return this
        }

        fun useRxJava(): Builder {
            retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            return this
        }

        fun callAdapter(): Builder {
            retrofitBuilder.addCallAdapterFactory(TaskCallAdapterFactory.get())
            return this
        }

        fun addConverterFactory(): Builder {
            val gson: Gson = GsonBuilder()
                .registerTypeAdapter(Boolean::class.java, BooleanGsonDeserializer())
                .registerTypeAdapter(String::class.java, StringJsonDeserializer())
                .registerTypeAdapter(Double::class.java, DoubleJsonDeserializer())
                .registerTypeAdapter(Float::class.java, FloatJsonDeserializer())
                .registerTypeAdapter(Long::class.java, LongJsonDeserializer())
                .registerTypeAdapter(Int::class.java, IntJsonDeserializer())
                .create()
            retrofitBuilder.addConverterFactory(JsonConverterFactory.create(gson))
            return this
        }

        fun build(): RetrofitManager {
            manager.retrofit = retrofitBuilder.build()
            return get()
        }
    }

    fun setUrl(url: String): RetrofitManager {
        BaseUrl.get().useDefaultUrl = false
        BaseUrl.get().baseUrl = url
        return this
    }

    fun <T> create(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }
}