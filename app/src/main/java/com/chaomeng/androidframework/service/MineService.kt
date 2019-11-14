package com.chaomeng.androidframework.service

import com.chaomeng.androidframework.bean.ProjectBean
import com.chaomeng.androidframework.bean.WanAndroidResponse
import com.chaomeng.androidframework.http.Response
import com.chaomeng.retrofit.Task
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MineService {

    @GET("/lg/collect/list/{page}/json")
    fun queryCollect(@Path("page") page: Int, @Header("Cookie") cookie: String): Task<WanAndroidResponse<ProjectBean>>
}