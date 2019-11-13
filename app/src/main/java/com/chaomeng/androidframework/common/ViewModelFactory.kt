package com.chaomeng.androidframework.common

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val lifecycleOwner: LifecycleOwner) : ViewModelProvider.Factory {

    private var appContext: Context? = null

    constructor(context: Context, lifecycleOwner: LifecycleOwner) : this(lifecycleOwner) {
        appContext = context.applicationContext
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return try {
            modelClass.getDeclaredConstructor(LifecycleOwner::class.java).newInstance(lifecycleOwner)
        } catch (e: NoSuchMethodException) {
            if (appContext != null) {
                modelClass.getDeclaredConstructor(LifecycleOwner::class.java, Context::class.java).newInstance(lifecycleOwner, appContext)
            } else {
                throw IllegalArgumentException("Context is null !! please call ViewModelFactory(context: Context, lifecycleOwner: LifecycleOwner) to set a Context ")
            }
        }
    }


}