package com.chaomeng.retrofit

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ThreadSwitcher<T> : ObservableTransformer<T, T> {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
             //订阅关系取消时，解除上游对下游的引用
            .onTerminateDetach()
    }
}