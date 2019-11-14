package com.chaomeng.androidframework.ui.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.chaomeng.androidframework.RequestManager
import com.chaomeng.androidframework.bean.*
import com.chaomeng.androidframework.common.BaseViewModel
import com.chaomeng.androidframework.http.Response
import com.chaomeng.androidframework.http.ResponseCallback
import com.chaomeng.androidframework.service.LoginService
import com.chaomeng.androidframework.utils.CacheManager
import com.chaomeng.androidframework.utils.RequestCallback
import com.chaomeng.retrofit.RetrofitManager

class MainViewModel(private val lifecycleOwner: LifecycleOwner) : BaseViewModel(lifecycleOwner) {

    val isLogout = MutableLiveData<Boolean>()
    val bannerList = MutableLiveData<List<Banner>>()
    val systemList = MutableLiveData<List<SystemBean>>()
    val categoryList = MutableLiveData<List<Category>>()
    val projectList = MutableLiveData<List<ProjectBean.Project>>()
    val articleList = MutableLiveData<List<ArticleBean.Article>>()

    fun queryBanner() {
        RequestManager.get()
            .getRequest()
            .bindLifecycle(lifecycleOwner)
            .setType<Response<List<Banner>>>()
            .get("https://www.wanandroid.com/banner/json", object: ResponseCallback<Response<List<Banner>>>() {
                override fun onSuccess(data: Response<List<Banner>>?) {
                    data?.let {
                        bannerList.postValue(it.data)
                    }
                }

            })
    }

    fun queryData(pageIndex: Int = 0) {
        val url = "https://www.wanandroid.com/article/list/$pageIndex/json"
        RequestManager.get()
            .getRequest()
            .bindLifecycle(lifecycleOwner)
            .setType<Response<ArticleBean>>()
            .get(url, object: ResponseCallback<Response<ArticleBean>>() {
                override fun onSuccess(data: Response<ArticleBean>?) {
                    data?.let {
                        articleList.postValue(it.data.datas)
                    }
                }

            })
    }

    fun queryCategory() {
        val url = "https://www.wanandroid.com/project/tree/json"
        RequestManager.get()
            .getRequest()
            .bindLifecycle(lifecycleOwner)
            .setType<Response<List<Category>>>()
            .get(url, object: ResponseCallback<Response<List<Category>>>() {
                override fun onSuccess(data: Response<List<Category>>?) {
                    data?.let {
                        categoryList.postValue(it.data)
                    }
                }

            })
    }

    fun queryProject(id: String, pageIndex: Int = 0) {
        val url = "https://www.wanandroid.com/project/list/$pageIndex/json?cid=$id"
        RequestManager.get()
            .getRequest()
            .bindLifecycle(lifecycleOwner)
            .setType<Response<ProjectBean>>()
            .get(url, object: ResponseCallback<Response<ProjectBean>>() {
                override fun onSuccess(data: Response<ProjectBean>?) {
                    data?.let {
                        projectList.postValue(it.data.datas)
                    }
                }

            })
    }

    fun querySystem() {
        val url = "https://www.wanandroid.com/tree/json"
        RequestManager.get()
            .getRequest()
            .bindLifecycle(lifecycleOwner)
            .setType<Response<List<SystemBean>>>()
            .get(url, object: ResponseCallback<Response<List<SystemBean>>>() {
                override fun onSuccess(data: Response<List<SystemBean>>?) {
                    data?.let {
                        systemList.postValue(it.data)
                    }
                }

            })
    }

    fun logout() {
        RetrofitManager.get()
            .setUrl("https://www.wanandroid.com/")
            .create(LoginService::class.java)
            .logout()
            .bindLifecycle(lifecycleOwner)
            .execute(object: RequestCallback<WanAndroidResponse<Any>>() {
                override fun onSuccess(data: WanAndroidResponse<Any>?) {
                    CacheManager.get().clear()
                    isLogout.postValue(true)
                }

            })
    }
}