package com.chaomeng.retrofit

import com.chaomeng.common.HttpCode

interface ApiErrorException {

    companion object {
        val CODE_DEFAULT = "-1"
        val CODE_JSON_PARSE_ERR = "-2"
        val CODE_SESSION_EXPIRED = "9998"
        val CODE_ACCOUNT_EXCEPTION = "7777"
    }

    fun onError(t: Throwable)
    fun onError(httpCode: HttpCode, businessCode: String?, error: String?)
}