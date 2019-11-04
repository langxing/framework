package com.chaomeng.androidframework.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.chaomeng.androidframework.App

class CacheManager private constructor(){
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var cacheName: String? = null

    companion object {
        private val instance: CacheManager by lazy {
            CacheManager()
        }

        @JvmStatic
        fun get(): CacheManager {
            return instance
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun initCache(key: String = App.getApp().packageName): CacheManager {
        if (key != cacheName) {
            cacheName = key
            sharedPreferences = App.getApp().applicationContext.getSharedPreferences(key, Context.MODE_PRIVATE)
            editor = sharedPreferences?.edit()
        }
        return instance
    }

    fun putString(key: String, value: String): CacheManager {
        if (sharedPreferences == null) {
            throw NullPointerException("请先调用initCache初始化")
        }
        editor?.let {
            it.putString(key, value)
        }
        return instance
    }

    fun getString(key: String): String? {
        if (sharedPreferences == null) {
            throw NullPointerException("请先调用initCache初始化")
        }
        return sharedPreferences?.getString(key, "")
    }

    fun commit() {
        if (editor == null) {
            throw NullPointerException("请先调用initCache初始化")
        }
        editor?.commit()
    }
}