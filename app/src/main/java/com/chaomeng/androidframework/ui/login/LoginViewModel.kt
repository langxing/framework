package com.chaomeng.androidframework.ui.login

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.chaomeng.androidframework.bean.LoginInfo
import com.chaomeng.androidframework.bean.WanAndroidResponse
import com.chaomeng.androidframework.common.BaseViewModel
import com.chaomeng.androidframework.service.LoginService
import com.chaomeng.androidframework.utils.RequestCallback
import com.chaomeng.retrofit.RetrofitManager

class LoginViewModel(private val lifecycleOwner: LifecycleOwner) : BaseViewModel(lifecycleOwner) {

    val cookie = MutableLiveData<String>()
    val loginInfo = MutableLiveData<LoginInfo>()

    fun login(account: String, password: String) {
        RetrofitManager.get()
            .setUrl("https://www.wanandroid.com/")
            .create(LoginService::class.java)
            .login(account, password)
            .bindLifecycle(lifecycleOwner)
            .execute(object: RequestCallback<WanAndroidResponse<LoginInfo>>() {

                override fun onResponse(response: retrofit2.Response<WanAndroidResponse<LoginInfo>>) {
                    super.onResponse(response)
                    val result = response.body()
                    if (result?.errorCode == "0") {
                        val str = response.headers().value(1)
                        cookie.postValue(str)
                        loginInfo.postValue(result.data)
                    }
                }

                override fun onSuccess(data: WanAndroidResponse<LoginInfo>?) {

                }

            })

    }
}