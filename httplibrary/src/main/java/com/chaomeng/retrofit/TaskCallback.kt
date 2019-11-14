package com.chaomeng.retrofit

import retrofit2.Response

interface TaskCallback<T> : ApiErrorException {

    fun onSuccess(data: T?)
    fun onResponse(response: Response<T>)

}