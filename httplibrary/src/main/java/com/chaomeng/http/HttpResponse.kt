package com.chaomeng.http

import android.os.Handler
import android.os.Looper
import com.chaomeng.HttpModule
import android.text.TextUtils
import android.widget.Toast
import com.chaomeng.jsondeserializer.*
import com.chaomeng.retrofit.HttpState
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.Exception
import java.lang.reflect.Type

abstract class HttpResponse<T: BaseResponse<*>> : IResponse<T> {

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

    override fun onFailed(code: String? , msg: String?) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(HttpModule.newInstance.getApplication(), msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResponse(data: String?, type: Type) {
        if (data?.isNotEmpty() == true) {
            val state = gson.fromJson(data, HttpState::class.java)
            if (state.code != "200") {
                handler.post {
                    onFailed(state.code, state.msg)
                    onComplete()
                }
            } else {
                try {
                    val result = gson.fromJson<T>(data, type)
                    handler.post {
                        onSuccess(result)
                        onComplete()
                    }
                } catch (e: Exception) {
                    handler.post {
                        onFailed("-1", e.message)
                        onComplete()
                    }
                }
            }
        } else {
            handler.post {
                onFailed("-1", "服务器异常")
                onComplete()
            }
        }
    }

}