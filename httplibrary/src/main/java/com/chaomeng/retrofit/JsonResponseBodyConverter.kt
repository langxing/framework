package com.chaomeng.retrofit

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import okhttp3.internal.Util.UTF_8
import retrofit2.Converter
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

class JsonResponseBodyConverter<T>(private val gson: Gson?, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T? {
        val data = value.string()
        // 此判断用于解决数据结构完全不一致的问题，但会影响扩展性，自己取舍
//        val state = gson?.fromJson(data, HttpState::class.java)
//        if (state?.isSuccess() == false) {
//            value.close()
//            throw RuntimeException(if (TextUtils.isEmpty(state.message)) state.msg else state.message)
//        }
        val contentType = value.contentType()
        val charset = if (contentType != null) contentType.charset(UTF_8) else UTF_8
        val inputStream = ByteArrayInputStream(data.toByteArray())
        val reader = InputStreamReader(inputStream, charset)
        val jsonReader = gson?.newJsonReader(reader)
        value.use {
            return adapter.read(jsonReader)
        }
    }
}