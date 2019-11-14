package com.chaomeng.androidframework.ui.collect

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.chaomeng.androidframework.RequestManager
import com.chaomeng.androidframework.bean.ProjectBean
import com.chaomeng.androidframework.common.BaseViewModel
import com.chaomeng.androidframework.common.Constant
import com.chaomeng.androidframework.http.Response
import com.chaomeng.androidframework.http.ResponseCallback
import com.chaomeng.androidframework.utils.CacheManager

class CollectViewModel(private val lifecycleOwner: LifecycleOwner) : BaseViewModel(lifecycleOwner) {

    val projectList = MutableLiveData<List<ProjectBean.Project>>()

    fun queryCollect(pageIndex: Int = 0) {
        CacheManager.get().getString(Constant.KEY_COOKIE)?.let {
            RequestManager.get()
                .getRequest()
                .bindLifecycle(lifecycleOwner)
                .setType<Response<ProjectBean>>()
                .addHeader("Cookie", it)
                .get("https://www.wanandroid.com/lg/collect/list/$pageIndex/json",
                    object: ResponseCallback<Response<ProjectBean>>() {
                        override fun onSuccess(data: Response<ProjectBean>?) {
                            data?.let {
                                projectList.postValue(data.data.datas)
                            }
                        }
                })
//            RetrofitManager.get()
//                .setUrl("https://www.wanandroid.com/")
//                .create(MineService::class.java)
//                .queryCollect(pageIndex, it)
//                .bindLifecycle(lifecycleOwner)
//                .execute(object: RequestCallback<WanAndroidResponse<ProjectBean>>() {
//                    override fun onSuccess(data: WanAndroidResponse<ProjectBean>?) {
//                        data?.let {
//                            projectList.postValue(data.data?.datas)
//                        }
//                    }
//
//                })
        }
    }
}