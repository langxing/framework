package com.chaomeng.androidframework.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.chaomeng.androidframework.R


class ImageLoader {

    private var requestManager: RequestManager? = null
    private var requestBuilder: RequestBuilder<*>? = null
    private var requestOptions: BaseRequestOptions<*>? = null

    fun with(view: View): ImageLoader {
        requestManager = Glide.with(view)
        return this
    }

    fun with(activity: Activity): ImageLoader {
        requestManager = Glide.with(activity)
        return this
    }

    fun with(fragment: Fragment): ImageLoader {
        requestManager = Glide.with(fragment)
        return this
    }

    fun with(context: Context): ImageLoader {
        requestManager = Glide.with(context)
        return this
    }

    fun manager(options: (requestmanager: RequestManager) -> RequestBuilder<*>): ImageLoader {
        requestManager?.let {
            requestBuilder = options.invoke(it)
        }
        return this
    }

    fun load(url: String): ImageLoader {
        requestBuilder = if (requestBuilder == null) {
            requestManager?.load(url)
        } else {
            requestBuilder?.load(url)
        }
        return this
    }

    /**
     * 如果调用了options/defaultOptions方法,必须在options/defaultOptions方法后面调用，
     * 否则会被覆盖导致circle方法失效
     */
    @SuppressLint("CheckResult")
    fun circle(): ImageLoader {
        val requestOptions = RequestOptions.bitmapTransform(CircleCrop())
        requestBuilder?.apply(requestOptions)
        return this
    }

    /**
     * 如果调用了options/defaultOptions方法,必须在options/defaultOptions方法后面调用，
     * 否则会被覆盖导致round方法失效
     */
    @SuppressLint("CheckResult")
    fun round(radius: Int = 5): ImageLoader {
        //设置图片圆角角度
        val roundedCorners = RoundedCorners(radius)
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        val options = RequestOptions.bitmapTransform(roundedCorners)
        requestBuilder?.apply(options)
        return this
    }

    fun options(options: (requestBuilder: RequestBuilder<*>) -> BaseRequestOptions<*>): ImageLoader {
        requestBuilder?.let {
            requestOptions = options.invoke(it)
        }
        return this
    }

    fun defaultOptions(options: ((requestBuilder: RequestBuilder<*>) -> BaseRequestOptions<*>)? = null): ImageLoader {
        requestBuilder = requestBuilder?.placeholder(R.drawable.ic_launcher_foreground)?.
                error(R.drawable.ic_launcher_foreground)?.
                centerCrop()
        options?.let {
            requestBuilder?.let {
                requestOptions = options.invoke(it)
            }
        }
        return this
    }

    fun into(imageView: ImageView) {
        requestBuilder?.into(imageView)
    }

    companion object {
        fun get(): ImageLoader {
            return ImageLoader()
        }
    }

    fun loadImage(url: String, imageView: ImageView) {
        Glide.with(imageView)
                .load(url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(imageView)
    }

}