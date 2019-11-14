package com.chaomeng.androidframework.ui.test

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaomeng.androidframework.R
import com.chaomeng.androidframework.adapter.MeituAdapter
import com.chaomeng.androidframework.bean.LoginInfo
import com.chaomeng.androidframework.bean.Meitu
import com.chaomeng.androidframework.bean.UserInfo
import com.chaomeng.androidframework.bean.WanAndroidResponse
import com.chaomeng.androidframework.http.BaseResponse
import com.chaomeng.androidframework.service.HomeService
import com.chaomeng.androidframework.utils.RequestCallback
import com.chaomeng.androidframework.widget.LoadingDialog
import com.chaomeng.androidframework.http.RequestObserver
import com.chaomeng.retrofit.RetrofitManager
import com.chaomeng.androidframework.http.TaskCallbackImpl
import com.chaomeng.retrofit.ThreadSwitcher
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_retrofit.*

@SuppressLint("Registered")
class RetrofitActivity : AppCompatActivity() {

    private val meituList = ObservableArrayList<Meitu>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit)
        val dialog = LoadingDialog()
        tvGetObject.setOnClickListener {
            RetrofitManager.get()
                .create(HomeService::class.java)
                .login()
                .onStart {
                    dialog.show(supportFragmentManager, javaClass.name)
                }
                .onComplete {
                    dialog.dismiss()
                }
                .bindLifecycle(this)
                .execute(object : TaskCallbackImpl<BaseResponse<LoginInfo>>() {
                    override fun onSuccess(data: BaseResponse<LoginInfo>?) {
                        tvData.text = "${data?.data?.toString()}"
                    }

                })
        }
        tvGetList.setOnClickListener {
            RetrofitManager.get()
                .create(HomeService::class.java)
                .getList()
                .bindLifecycle(this)
                .execute(object: TaskCallbackImpl<BaseResponse<List<Meitu>>>() {
                    override fun onSuccess(data: BaseResponse<List<Meitu>>?) {
                        if (data?.data != null) {
                            meituList.addAll(data.data!!)
                            recyclerView.adapter = MeituAdapter(meituList)
                            recyclerView.layoutManager = LinearLayoutManager(this@RetrofitActivity)
                            recyclerView.addItemDecoration(DividerItemDecoration(this@RetrofitActivity, LinearLayoutManager.VERTICAL))
                        }
                    }

                })

        }
        tvSwitchUrl.setOnClickListener {
            RetrofitManager.get()
                .setUrl("https://www.wanandroid.com")
                .create(HomeService::class.java)
                .login("15820762583", "420323")
                .bindLifecycle(this)
                .execute(object: RequestCallback<WanAndroidResponse<UserInfo>>() {
                    override fun onSuccess(data: WanAndroidResponse<UserInfo>?) {
                        tvData.text = "${data?.data?.toString()}"
                    }

                })
        }
        tvRxjava.setOnClickListener {
            RetrofitManager.get()
                .create(HomeService::class.java)
                .loginByPhone()
                .compose(ThreadSwitcher())
                .subscribe(object: RequestObserver<BaseResponse<LoginInfo>>(this) {
                    override fun onSuccess(response: BaseResponse<LoginInfo>?) {
                        tvData.text = "${response?.data?.toString()}"
                    }

                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        dialog.show(supportFragmentManager, javaClass.name)
                    }

                    override fun onComplete() {
                        super.onComplete()
                        dialog.dismiss()
                    }
                })
        }
    }
}