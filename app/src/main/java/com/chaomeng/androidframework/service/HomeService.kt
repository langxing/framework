package com.chaomeng.androidframework.service

import com.chaomeng.androidframework.bean.LoginInfo
import com.chaomeng.androidframework.bean.Meitu
import com.chaomeng.androidframework.bean.UserInfo
import com.chaomeng.androidframework.bean.WanAndroidResponse
import com.chaomeng.common.BaseResponse
import com.chaomeng.retrofit.RequestObserver
import com.chaomeng.retrofit.Task
import io.reactivex.Observable
import io.reactivex.Observer
import retrofit2.http.*

interface HomeService {

    @POST("/user/login")
    @FormUrlEncoded
    fun login(@Field("username") username: String, @Field("password") password: String): Task<WanAndroidResponse<UserInfo>>

    @GET("/meituApi?page=1")
    fun getList(): Task<BaseResponse<List<Meitu>>>

    @GET("/login")
    fun login(@Query("key") key: String = "00d91e8e0cca2b76f515926a36db68f5",
              @Query("phone") phone: String = "13594347817",
              @Query("passwd") passwd: String = "123456"): Task<BaseResponse<LoginInfo>>

    @GET("/login")
    fun loginByPhone(@Query("key") key: String = "00d91e8e0cca2b76f515926a36db68f5",
              @Query("phone") phone: String = "13594347817",
              @Query("passwd") passwd: String = "123456"): Observable<BaseResponse<LoginInfo>>
}