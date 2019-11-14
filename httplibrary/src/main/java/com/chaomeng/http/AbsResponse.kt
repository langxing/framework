package com.chaomeng.http

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import com.chaomeng.HttpModule
import com.chaomeng.jsondeserializer.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Response
import java.lang.reflect.Type

abstract class AbsResponse<T> : IResponse<T> {

    companion object {
        val handler = Handler(Looper.getMainLooper())
        val gson: Gson = GsonBuilder()
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

    override fun onFailed(response: Response?, code: String? , msg: String?) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(HttpModule.newInstance.getApplication(), msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResponse(response: Response, type: Type) {
        val data = response.body()?.string()
        if (response.code() == 200) {
            try {
                val result = gson.fromJson<T>(data, type)
                handler.post {
                    onSuccess(result)
                    onComplete()
                }
            } catch (e: Exception) {
                handler.post {
                    onFailed(response, "-1", e.message)
                    onComplete()
                }
            }
        } else {
            handler.post {
                onFailed(response, "-1", "服务器异常")
                onComplete()
            }
        }
    }
}