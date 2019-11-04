package com.chaomeng.androidframework.bean

data class WanAndroidResponse<T>(val errorCode: String, val errorMsg: String, val data: T?)