package com.chaomeng.androidframework.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.chaomeng.androidframework.App
import java.io.*

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
        editor?.putString(key, value)
        return instance
    }

    fun getString(key: String): String? {
        if (sharedPreferences == null) {
            throw NullPointerException("请先调用initCache初始化")
        }
        return sharedPreferences?.getString(key, "")
    }

    fun <T> putObject(key: String, t: T): CacheManager {
        try {
            val baos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(baos)
            //把对象写到流里
            oos.writeObject(t)
            val temp = String(Base64.encode(baos.toByteArray(), Base64.DEFAULT))
            editor?.putString(key, temp)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return instance
    }

    fun <T> getObject(key: String): T? {
        val temp = sharedPreferences?.getString(key, "")
        val bais =  ByteArrayInputStream(Base64.decode(temp?.toByteArray(), Base64.DEFAULT))
        var t: T? = null
        try {
            val ois = ObjectInputStream(bais)
            t = ois.readObject() as T?
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return t
    }

    fun commit() {
        if (editor == null) {
            throw NullPointerException("请先调用initCache初始化")
        }
        editor?.commit()
    }
}