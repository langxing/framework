package com.chaomeng.http

import android.os.Handler
import android.os.Looper
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
import com.chaomeng.common.DownloadListener
import com.chaomeng.utils.NetworkManager
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class OkHttpRequest private constructor() : IRequest, LifecycleObserver {

    private var handler: Handler = Handler(Looper.getMainLooper())

    private var headers = IdentityHashMap<String, Any>()
    private var params = HashMap<String, Any>()
    private var currentCall: Call? = null
    private var tag: String? = null

    lateinit var dataType: Type

    companion object {
        private var globalHeaders = IdentityHashMap<String, Any>()
        private var globalParams = HashMap<String, Any>()
        private var okHttpClient: OkHttpClient? = null
        private var requestQueue = ArrayList<Call>()

        fun get() = OkHttpRequest()
    }

    fun bindLifecycle(activity: AppCompatActivity): OkHttpRequest {
        activity.lifecycle.addObserver(this)
        return this
    }

    fun bindLifecycle(fragment: Fragment): OkHttpRequest {
        fragment.lifecycle.addObserver(this)
        return this
    }

    fun bindLifecycle(lifecycleOwner: LifecycleOwner): OkHttpRequest {
        lifecycleOwner.lifecycle.addObserver(this)
       return this
    }

    fun bindLifecycle(view: View): OkHttpRequest {
        view.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                if (currentCall?.isCanceled == false) {
                    currentCall?.cancel()
                }
            }

            override fun onViewAttachedToWindow(v: View?) {

            }

        })
        return this
    }

    override fun <T> get(url: String, response: IResponse<T>) {
        if (!NetworkManager.get().checkNetwork()) {
            Toast.makeText(HttpModule.newInstance.getApplication(), "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show()
            return
        }
        if (okHttpClient == null) {
            throw NullPointerException("请初始化okHttpClient")
        }
        handler.post {
            response.onStart()
        }
        val builder = Request.Builder()
        val parseUrl = StringBuilder(url)
        globalParams.entries.forEach { entry ->
            if (parseUrl.contains("?")) {
                parseUrl.append("&${entry.key}=${entry.value}")
            } else {
                parseUrl.append("?${entry.key}=${entry.value}")
            }
        }
        globalHeaders.entries.forEach { entry ->
            builder.addHeader(entry.key, entry.value.toString())
        }
        params.forEach {
            if (parseUrl.contains("?")) {
                parseUrl.append("&${it.key}=${it.value}")
            } else {
                parseUrl.append("?${it.key}=${it.value}")
            }
        }
        headers.forEach {
            builder.addHeader(it.key, it.value.toString())
        }
        val requestUrl =
            if (parseUrl.endsWith("&")) {
            parseUrl.substring(0, parseUrl.length - 1)
        } else parseUrl
        if (TextUtils.isEmpty(tag)) {
            tag = requestUrl.toString()
        }
        builder.tag(tag)
        val request = builder.url(requestUrl.toString())
            .get()
            .build()
        okHttpClient?.let {
            val call = it.newCall(request)
            currentCall = call
            requestQueue.add(call)
            call.enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    handler.post {
                        response.onFailed(code = "-1", msg = e.message)
                    }
                }

                override fun onResponse(call: Call, resp: Response) {
                    response.onResponse(resp, type = dataType)
                }

            })
        }
    }

    override fun <T> post(url: String, response: IResponse<T>) {
        if (!NetworkManager.get().checkNetwork()) {
            Toast.makeText(HttpModule.newInstance.getApplication(), "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show()
            return
        }
        if (okHttpClient == null) {
            throw NullPointerException("请初始化okHttpClient")
        }
        handler.post {
            response.onStart()
        }
        val builder = FormBody.Builder()
        val requestBuilder = Request.Builder().url(url)
        globalParams.entries.forEach { entry ->
            builder.add(entry.key, entry.value.toString())
        }
        globalHeaders.entries.forEach { entry ->
            requestBuilder.addHeader(entry.key, entry.value.toString())
        }
        params.forEach {
            builder.add(it.key, it.value.toString())
        }
        if (TextUtils.isEmpty(tag)) {
            tag = url
        }
        requestBuilder.tag(tag)
        val request = requestBuilder.post(builder.build()).build()
        okHttpClient?.let {
            val call = it.newCall(request)
            currentCall = call
            requestQueue.add(call)
            call.enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    handler.post {
                        response.onFailed(code = "-1", msg = e.message)
                    }
                }

                override fun onResponse(call: Call, resp: Response) {
                    response.onResponse(resp,  type = dataType)
                }

            })
        }
    }

    override fun <T> put(url: String, response: IResponse<T>) {
        if (!NetworkManager.get().checkNetwork()) {
            Toast.makeText(HttpModule.newInstance.getApplication(), "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show()
            return
        }
        if (okHttpClient == null) {
            throw NullPointerException("请初始化okHttpClient")
        }
        handler.post {
            response.onStart()
        }
        val builder = FormBody.Builder()
        val requestBuilder = Request.Builder().url(url)
        globalParams.entries.forEach { entry ->
            builder.add(entry.key, entry.value.toString())
        }
        globalHeaders.entries.forEach { entry ->
            requestBuilder.addHeader(entry.key, entry.value.toString())
        }
        params.forEach {
            builder.add(it.key, it.value.toString())
        }
        if (TextUtils.isEmpty(tag)) {
            tag = url
        }
        requestBuilder.tag(tag)
        val request = requestBuilder.put(builder.build()).build()
        okHttpClient?.let {
            val call = it.newCall(request)
            currentCall = call
            requestQueue.add(call)
            call.enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    handler.post {
                        response.onFailed(code = "-1", msg = e.message)
                    }
                }

                override fun onResponse(call: Call, resp: Response) {
                    response.onResponse(resp,  type = dataType)
                }

            })
        }
    }

    override fun <T> delete(url: String, response: IResponse<T>) {
        if (!NetworkManager.get().checkNetwork()) {
            Toast.makeText(HttpModule.newInstance.getApplication(), "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show()
            return
        }
        if (okHttpClient == null) {
            throw NullPointerException("请初始化okHttpClient")
        }
        handler.post {
            response.onStart()
        }
        val builder = FormBody.Builder()
        val requestBuilder = Request.Builder().url(url)
        globalParams.entries.forEach { entry ->
            builder.add(entry.key, entry.value.toString())
        }
        globalHeaders.entries.forEach { entry ->
            requestBuilder.addHeader(entry.key, entry.value.toString())
        }
        params.forEach {
            builder.add(it.key, it.value.toString())
        }
        if (TextUtils.isEmpty(tag)) {
            tag = url
        }
        requestBuilder.tag(tag)
        val request = requestBuilder.delete(builder.build()).build()
        okHttpClient?.let {
            val call = it.newCall(request)
            currentCall = call
            requestQueue.add(call)
            call.enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    handler.post {
                        response.onFailed(code = "-1", msg = e.message)
                    }
                }

                override fun onResponse(call: Call, resp: Response) {
                    response.onResponse(resp,  type = dataType)
                }

            })
        }
    }

    override fun download(url: String, file: File, listener: DownloadListener) {
        if (!NetworkManager.get().checkNetwork()) {
            Toast.makeText(HttpModule.newInstance.getApplication(), "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show()
            return
        }
        if (okHttpClient == null) {
            throw NullPointerException("请初始化okHttpClient")
        }
        if (!file.exists()) {
            listener.onError("文件不存在，请检查文件路径是否正确")
            return
        }
        listener.onStart()
        val builder = Request.Builder().url(url)
        if (TextUtils.isEmpty(tag)) {
            tag = url
        }
        val request = builder.tag(tag).build()
        okHttpClient?.let {
            val call = it.newCall(request)
            requestQueue.add(call)
            call.enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    handler.post {
                        listener.onError("下载失败")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    var outputStream: FileOutputStream? = null
                    var inputStream: InputStream? = null
                    var current = 0L
                    var len: Int

                    val array = ByteArray(2048)

                    try {
                        val totalLength = response.body()?.contentLength() ?: 0
                        inputStream = response.body()?.byteStream()
                        if (inputStream != null) {
                            outputStream = FileOutputStream(file)
                            while (inputStream.read(array).also { len = it } != -1) {
                                current += len
                                outputStream.write(array, 0, len)
                                handler.post {
                                    listener.onDownloadIng(current, totalLength)
                                }
                            }
                            inputStream.close()
                            outputStream.flush()
                            outputStream.close()
                            handler.post {
                                listener.onComplete()
                            }
                        }
                    } catch (e: IOException) {
                        handler.post {
                            listener.onError("下载失败")
                        }
                    } finally {
                        inputStream?.close()
                        outputStream?.flush()
                        outputStream?.close()
                    }
                }

            })
        }
    }

    override fun upload(url: String, file: File, mediaType: MediaType, listener: UploadListener) {
        if (!NetworkManager.get().checkNetwork()) {
            Toast.makeText(HttpModule.newInstance.getApplication(), "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show()
            return
        }
        if (okHttpClient == null) {
            throw NullPointerException("请初始化okHttpClient")
        }
        if (!file.exists()) {
            listener.onError("文件不存在，请检查文件路径是否正确")
            return
        }
        listener.onStart()
        val requestBody = ProgressRequestBody(file, mediaType) { progressValue, maxValue ->
            listener.onUploadIng(progressValue, maxValue)
        }
        val builder = Request.Builder()
        if (TextUtils.isEmpty(tag)) {
            tag = url
        }
        val request = builder.post(requestBody).url(url).tag(tag).build()
        okHttpClient?.let {
            val call = it.newCall(request)
            requestQueue.add(call)
            call.enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    handler.post {
                        listener.onError("上传失败，请稍后重试")
                    }
                }

                override fun onResponse(call: Call, resp: Response) {
                    handler.post {
                        listener.onComplete()
                    }
                }

            })
        }
    }

    override fun addHeader(key: String, value: Any): IRequest {
        headers[key] = value
        return this
    }

    override fun addParam(key: String, value: Any): IRequest {
        params[key] = value
        return this
    }

    override fun setTag(requestTag: String): IRequest {
        tag = requestTag
        return this
    }

    override fun cancel(requestTag: String) {
        var call: Call? = null
        requestQueue.forEach {
            if (it.request().tag() == requestTag) {
                it.cancel()
                call = it
            }
        }
        requestQueue.remove(call)
    }

    inline fun <reified T> setType(type: Type = object: TypeToken<T>(){}.type): IRequest {
        dataType = type
        return this
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (currentCall?.isCanceled == false) {
            currentCall?.cancel()
        }
    }

    class Builder {

        private var request: OkHttpRequest = get()
        private var builder: OkHttpClient.Builder = OkHttpClient().newBuilder()

        fun addParams(params: Pair<String, Any>): Builder {
            globalParams[params.first] = params.second
            return this
        }

        fun addHeader(header: Pair<String, Any>): Builder {
            globalHeaders[header.first] = header.second
            return this
        }

        fun connectTimeout(time: Long = 30, timeUnit: TimeUnit = TimeUnit.SECONDS): Builder {
            builder.connectTimeout(time, timeUnit)
            return this
        }

        fun readTimeout(time: Long = 30, timeUnit: TimeUnit = TimeUnit.SECONDS): Builder {
            builder.readTimeout(time, timeUnit)
            return this
        }

        fun writeTimeout(time: Long = 30, timeUnit: TimeUnit = TimeUnit.SECONDS): Builder {
            builder.writeTimeout(time, timeUnit)
            return this
        }

        fun setSSLFactory(sslSocketFactory: SSLSocketFactory? = null, trustManager: X509TrustManager? = null): Builder {
            val sslFactory = sslSocketFactory ?: SSLSocketManager.get().defaultSSLSocketFactory()
            val manager = trustManager ?: SSLSocketManager.get().defaultTrustManager()
            sslFactory?.let {
                builder.sslSocketFactory(sslFactory, manager)
            }
            return this
        }

        fun setHostnameVerifier(hostnameVerifier: HostnameVerifier? = null): Builder {
            val verifier = hostnameVerifier ?: SSLSocketManager.TrustAllHostnameVerifier()
            builder.hostnameVerifier(verifier)
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {
            builder.addInterceptor(interceptor)
            return this
        }

        fun builder(): OkHttpClient.Builder {
            return builder
        }

        fun build(): IRequest {
            okHttpClient = builder.build()
            return request
        }
    }
}