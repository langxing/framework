package com.chaomeng.androidframework.utils

import com.chaomeng.androidframework.bean.WanAndroidResponse
import com.chaomeng.common.HttpCode
import com.chaomeng.retrofit.AbsTaskCallback
import retrofit2.Response

abstract class RequestCallback<T : WanAndroidResponse<*>> : AbsTaskCallback<T>() {

    private val CODE_SUCCESS = "0"

    override fun onSuccess(data: T?) {
        val code = data?.errorCode
        if (code == CODE_SUCCESS) {
            onSuccess(data)
        } else {
            onError(HttpCode.STATUS_OK, code, data?.errorMsg)
        }
    }

}