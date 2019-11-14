package com.chaomeng.androidframework.http

import com.chaomeng.common.HttpCode
import com.chaomeng.retrofit.AbsTaskCallback
import retrofit2.Response

abstract class TaskCallbackImpl<T : BaseResponse<*>> : AbsTaskCallback<T>() {

    companion object {
        const val CODE_SUCCESS = "200"
    }

    override fun onResponse(response: Response<T>) {
        val data = response.body()
        val httpCode = response.code()
        if (httpCode == 200) {
            val code = data?.code
            if (code == CODE_SUCCESS) {
                onSuccess(data)
            } else {
                onError(HttpCode.STATUS_OK, code, data?.msg)
            }
        } else {
            onError(HttpCode.mapIntValue(httpCode), null, response.message())
        }
    }

}