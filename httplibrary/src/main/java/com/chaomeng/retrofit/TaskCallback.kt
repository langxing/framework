package com.chaomeng.retrofit

import retrofit2.Response

interface TaskCallback<T> : ApiErrorException {

    companion object {
        const val CODE_SUCCESS = "200"
    }

    fun onSuccess(data: T?)
    fun onResponse(response: Response<T>)

}