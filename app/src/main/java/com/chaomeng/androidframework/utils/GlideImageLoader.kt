package com.chaomeng.androidframework.utils

import android.content.Context
import android.widget.ImageView
import com.youth.banner.loader.ImageLoader

abstract class GlideImageLoader<T> : ImageLoader() {

    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        context?.let {
            if (imageView != null) {
                com.chaomeng.androidframework.utils.ImageLoader.get()
                    .with(it)
                    .load(getUrl(path as T))
                    .defaultOptions()
                    .into(imageView)
            }
        }
    }

    abstract fun getUrl(t: T?): String
}