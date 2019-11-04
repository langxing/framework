package com.chaomeng.http

class BaseResponse<T> {

    var code: Int = 0
    var result: T? = null
    var message: String? = ""

}