package com.chaomeng.androidframework.http

data class Response<T>(val data: T,
                       val errorMsg: String,
                       val errorCode: String)