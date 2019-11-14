package com.chaomeng.androidframework.utils

import com.chaomeng.androidframework.bean.WanAndroidResponse
import com.chaomeng.common.HttpCode
import com.chaomeng.retrofit.AbsTaskCallback
import retrofit2.Response

abstract class RequestCallback<T : WanAndroidResponse<*>> : AbsTaskCallback<T>() {

    override fun onResponse(response: Response<T>) {
        val data = response.body()
        val httpCode = response.code()
        if (httpCode == 200) {
            val code = data?.errorCode
            if (code == "0") {
                onSuccess(data)
            } else {
                onError(HttpCode.STATUS_OK, code, data?.errorMsg)
            }
        } else {
            onError(HttpCode.mapIntValue(httpCode), null, response.message())
        }
    }

}