package com.chaomeng.retrofit

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.chaomeng.HttpModule
import com.chaomeng.utils.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Task<T>(private val call: Call<T>) : LifecycleObserver {

    private val handler = Handler(Looper.getMainLooper())

    private var onComplete: (() -> Unit)? = null
    private var onStart: (() -> Unit)? = null

    fun bindLifecycle(activity: AppCompatActivity): Task<T> {
        activity.lifecycle.addObserver(this)
        return this
    }

    fun bindLifecycle(lifecycleOwner: LifecycleOwner): Task<T> {
        lifecycleOwner.lifecycle.addObserver(this)
        return this
    }

    fun bindLifecycle(fragment: Fragment): Task<T> {
        fragment.lifecycle.addObserver(this)
        return this
    }

    fun bindLifecycle(view: View): Task<T> {
        view.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                if (call.isCanceled) {
                    call.cancel()
                }
            }

            override fun onViewAttachedToWindow(v: View?) {

            }

        })
        return this
    }

    fun onStart(onStart: (() -> Unit)? = null): Task<T> {
        this.onStart = onStart
        return this
    }

    fun onComplete(onComplete: (() -> Unit)? = null): Task<T> {
        this.onComplete = onComplete
        return this
    }

    fun setTag(tag: String) {

    }

    fun execute(callback: TaskCallback<T>) {
        if (!NetworkManager.get().checkNetwork()) {
            Toast.makeText(HttpModule.newInstance.getApplication(), "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show()
            return
        }
        handler.post {
            onStart?.invoke()
        }
        call.enqueue(object: Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                handler.post {
                    onComplete?.invoke()
                    callback.onError(t)
                }
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                handler.post {
                    onComplete?.invoke()
                    callback.onResponse(response)
                }
            }

        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (!call.isCanceled) {
            call.cancel()
        }
    }
}