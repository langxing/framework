package com.chaomeng.androidframework.http

import android.os.Handler
import android.os.Looper
import com.chaomeng.http.IResponse
import com.chaomeng.jsondeserializer.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

abstract class ResponseCallback<T : Response<*>> : IResponse<T> {

    companion object {
        private val handler = Handler(Looper.getMainLooper())
        private val gson: Gson = GsonBuilder()
            .registerTypeAdapter(Boolean::class.java, BooleanGsonDeserializer())
            .registerTypeAdapter(String::class.java, StringJsonDeserializer())
            .registerTypeAdapter(Double::class.java, DoubleJsonDeserializer())
            .registerTypeAdapter(Float::class.java, FloatJsonDeserializer())
            .registerTypeAdapter(Long::class.java, LongJsonDeserializer())
            .registerTypeAdapter(Int::class.java, IntJsonDeserializer())
            .create()
    }

    override fun onStart() {

    }

    override fun onComplete() {

    }

    override fun onResponse(data: String?, type: Type) {
        if (data?.isNotEmpty() == true) {
            val state = gson.fromJson(data, HttpCode::class.java)
            if (state.errorCode != "0") {
                handler.post {
                    onFailed(state.errorCode, state.errorMsg)
                    onComplete()
                }
            } else {
                try {
                    val result = gson.fromJson<T>(data, type)
                    handler.post {
                        onSuccess(result)
                    }
                } catch (e: Exception) {
                    handler.post {
                        onFailed("-1", e.message)
                    }
                } finally {
                    onComplete()
                }
            }
        } else {
            handler.post {
                onFailed("-1", "服务器异常")
                onComplete()
            }
        }
    }

    override fun onFailed(code: String?, msg: String?) {

    }
}