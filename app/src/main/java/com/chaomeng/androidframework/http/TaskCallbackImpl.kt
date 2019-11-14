package com.chaomeng.androidframework.http

import com.chaomeng.common.HttpCode
import com.chaomeng.retrofit.AbsTaskCallback

abstract class TaskCallbackImpl<T : BaseResponse<*>> : AbsTaskCallback<T>() {

    private val CODE_SUCCESS = "200"

    override fun onSuccess(data: T?) {
        val code = data?.code
        if (code == CODE_SUCCESS) {
            onSuccess(data)
        } else {
            onError(httpCode = HttpCode.STATUS_OK, businessCode = code, error = data?.msg)
        }
    }

}