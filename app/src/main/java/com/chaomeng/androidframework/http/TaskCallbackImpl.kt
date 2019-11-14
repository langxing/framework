package com.chaomeng.androidframework.http

import android.text.TextUtils
import android.widget.Toast
import com.chaomeng.HttpModule
import com.chaomeng.common.HttpCode
import com.chaomeng.retrofit.ApiErrorException
import com.chaomeng.retrofit.TaskCallback
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import retrofit2.Response
import java.lang.RuntimeException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException

abstract class TaskCallbackImpl<T : BaseResponse<*>> : TaskCallback<T> {

    companion object {
        const val CODE_SUCCESS = "200"
    }

    override fun onResponse(response: Response<T>) {
        val data = response.body()
        val httpCode = response.code()
        if (httpCode == 200) {
            val code = data?.code
            if (code == CODE_SUCCESS) {
                onSuccess(data)
            } else {
                onError(HttpCode.STATUS_OK, code, data?.msg)
            }
        } else {
            onError(HttpCode.mapIntValue(httpCode), null, response.message())
        }
    }

    override fun onError(t: Throwable) {
        if(t is JsonParseException || t is JSONException || t is ParseException) {
            val message = "数据解析异常"
            onError(HttpCode.STATUS_OK,
                ApiErrorException.CODE_JSON_PARSE_ERR, message)
        } else if(t is HttpException) {
            var message: String? = null

            when(t.code()){
                HttpCode.UNAUTHORIZED.intValue(), HttpCode.FORBIDDEN.intValue() -> {
                    message = "未授权或禁止访问: ${t.code()}"
                }
                HttpCode.NOT_FOUND.intValue(),
                HttpCode.REQUEST_TIMEOUT.intValue(),
                HttpCode.GATEWAY_TIMEOUT.intValue(),
                HttpCode.INTERNAL_SERVER_ERROR.intValue(),
                HttpCode.BAD_GATEWAY.intValue(),
                HttpCode.SERVICE_UNAVAILABLE.intValue() -> {
                    message = "服务器繁忙，请稍后再试"
                }
            }

            val httpCode = HttpCode.mapIntValue(t.code())
            onError(httpCode, null, message)
        } else if(t is UnknownHostException || t is ConnectException) {
            var message: String? = "域名解析失败"
            var code: HttpCode = HttpCode.DNS_ERROR

            if(t is ConnectException) {
                message = "网络连接失败，请检查当前网络配置"
                code = HttpCode.NETWORK_NOT_CONNECTED
            }

            onError(code, null, message)
        } else if (t is RuntimeException) {
            onError(HttpCode.STATUS_OK, null, t.message)
        } else {
            val message = "服务器繁忙，请稍后再试"
            val code: HttpCode = HttpCode.UNKNOWN

            onError(code, null, message)
        }
    }

    override fun onError(httpCode: HttpCode, businessCode: String?, error: String?) {
        if (!TextUtils.isEmpty(error)) {
            Toast.makeText(HttpModule.newInstance.getApplication(), error, Toast.LENGTH_SHORT).show()
        }
    }

}