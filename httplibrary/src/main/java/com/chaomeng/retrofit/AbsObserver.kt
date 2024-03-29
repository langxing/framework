package com.chaomeng.retrofit

import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.chaomeng.HttpModule
import com.chaomeng.common.HttpCode
import com.google.gson.JsonParseException
import io.reactivex.disposables.Disposable
import org.json.JSONException
import retrofit2.HttpException
import java.lang.RuntimeException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException

abstract class AbsObserver<T> : IObserver<T>, LifecycleObserver {

    private lateinit var disposable: Disposable

    constructor()

    constructor(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    constructor(activity: AppCompatActivity): this() {
        activity.lifecycle.addObserver(this)
    }

    constructor(fragment: Fragment): this() {
        fragment.lifecycle.addObserver(this)
    }

    constructor(view: View): this() {
        view.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                if (!disposable.isDisposed) {
                    disposable.dispose()
                }
            }

            override fun onViewAttachedToWindow(v: View?) {

            }

        })
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onError(t: Throwable) {
        if(t is JsonParseException || t is JSONException || t is ParseException) {
            val message = "数据解析异常"
            onError(
                HttpCode.STATUS_OK,
                ApiErrorException.CODE_JSON_PARSE_ERR, message)
        } else if(t is HttpException) {
            var message: String? = null

            when(t.code()){
                HttpCode.UNAUTHORIZED.intValue(), HttpCode.FORBIDDEN.intValue() -> {
                    message = "未授权或禁止访问: ${t.code()}"
                }
                HttpCode.NOT_FOUND.intValue(),
                HttpCode.REQUEST_TIMEOUT.intValue(),
                HttpCode.GATEWAY_TIMEOUT.intValue(),
                HttpCode.INTERNAL_SERVER_ERROR.intValue(),
                HttpCode.BAD_GATEWAY.intValue(),
                HttpCode.SERVICE_UNAVAILABLE.intValue() -> {
                    message = "服务器繁忙，请稍后再试"
                }
            }

            val httpCode = HttpCode.mapIntValue(t.code())
            onError(httpCode, null, message)
        } else if(t is UnknownHostException || t is ConnectException) {
            var message: String? = "域名解析失败"
            var code: HttpCode = HttpCode.DNS_ERROR

            if(t is ConnectException) {
                message = "网络连接失败，请检查当前网络配置"
                code = HttpCode.NETWORK_NOT_CONNECTED
            }

            onError(code, null, message)
        } else if (t is RuntimeException) {
            onError(HttpCode.STATUS_OK, null, t.message)
        } else {
            val message = "服务器繁忙，请稍后再试"
            val code: HttpCode = HttpCode.UNKNOWN

            onError(code, null, message)
        }
    }

    override fun onError(httpCode: HttpCode, businessCode: String?, error: String?) {
        if (!TextUtils.isEmpty(error)) {
            Toast.makeText(HttpModule.newInstance.getApplication(), error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onComplete() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}