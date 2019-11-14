package com.chaomeng.androidframework.http

class BaseResponse<T> {

    var data: T? = null
    var result: T? = null
    var code: String = "0"
    var msg: String? = null
    var message: String? = ""

}