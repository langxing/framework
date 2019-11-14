package com.chaomeng.androidframework.http

import com.chaomeng.http.AbsResponse
import com.chaomeng.retrofit.HttpState

abstract class ResponseCallback<T : Response<*>> : AbsResponse<T>() {

    override fun onFailed(response: okhttp3.Response?, code: String?, msg: String?) {
        super.onFailed(response, code, msg)
        if (response != null) {
            val data = response.body()?.string()
            // 解决data数据类型不一致问题
            val state = gson.fromJson(data, HttpState::class.java)
            handler.post {
                if (state.code != "200") {
                    onFailed(code = state.code, msg = state.msg)
                } else {
                    onFailed(code = state.code, msg = "数据异常")
                }
                onComplete()
            }
        }
    }

}