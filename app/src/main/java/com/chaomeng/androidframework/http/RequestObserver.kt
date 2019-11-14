package com.chaomeng.androidframework.http

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.chaomeng.common.HttpCode
import com.chaomeng.retrofit.AbsObserver

abstract class RequestObserver<T : BaseResponse<*>> : AbsObserver<T> {

    private val CODE_SUCCESS = "200"

    constructor(): super()

    constructor(fragment: Fragment): super(fragment)

    constructor(activity: AppCompatActivity): super(activity)

    constructor(lifecycleOwner: LifecycleOwner): super(lifecycleOwner)


    override fun onNext(t: T) {
        val code = t.code
        if (code == CODE_SUCCESS) {
            onSuccess(t)
        } else {
            onError(HttpCode.STATUS_OK, null, t.msg)
        }
    }

}