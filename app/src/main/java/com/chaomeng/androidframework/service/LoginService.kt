package com.chaomeng.androidframework.service

import com.chaomeng.androidframework.bean.LoginInfo
import com.chaomeng.androidframework.bean.WanAndroidResponse
import com.chaomeng.androidframework.http.Response
import com.chaomeng.retrofit.Task
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginService {

    @POST("user/login")
    @FormUrlEncoded
    fun login(@Field("username") username: String, @Field("password") password: String): Task<WanAndroidResponse<LoginInfo>>

    @GET("user/logout/json")
    fun logout(): Task<WanAndroidResponse<Any>>
}