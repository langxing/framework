package com.chaomeng.retrofit

import com.chaomeng.common.HttpCode
import retrofit2.Response

interface ApiErrorException {

    companion object {
        val CODE_DEFAULT = "-1"
        val CODE_JSON_PARSE_ERR = "-2"
        val CODE_SESSION_EXPIRED = "9998"
        val CODE_ACCOUNT_EXCEPTION = "7777"
    }

    fun onError(t: Throwable)
    /**
     * @param response 返回数据
     * @param httpCode http协议code
     * @param businessCode 业务返回code
     * @param error 错误信息
     */
    fun onError(response: Response<*>? = null, httpCode: HttpCode, businessCode: String?, error: String?)
}