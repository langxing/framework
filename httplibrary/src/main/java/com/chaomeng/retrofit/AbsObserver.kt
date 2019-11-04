package com.chaomeng.retrofit

import com.chaomeng.common.HttpCode
import io.reactivex.Observer

interface AbsObserver<T> : Observer<T> {

    fun onSuccess(response: T?)

    fun onError(httpCode: HttpCode, businessCode: String?, error: String?)
}