package com.chaomeng.http

import okhttp3.Response
import java.lang.reflect.Type

interface IResponse<T> {

    fun onStart()

    fun onComplete()

    fun onSuccess(data: T?)

    fun onResponse(data: Response, type: Type)

    fun onFailed(response: Response? = null, code: String? = "0", msg: String? = "")

}
