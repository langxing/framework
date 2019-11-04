package com.chaomeng.http

import java.lang.reflect.Type

interface IResponse<T> {

    fun onStart()

    fun onComplete()

    fun onSuccess(data: T?)

    fun onResponse(data: String?, type: Type)

    fun onFailed(code: String? = "0", msg: String? = "")

}
