package com.chaomeng.retrofit

class HttpState(val code: String, val msg: String, val message: String) {

    fun isSuccess(): Boolean {
        return code == "200"
    }
}