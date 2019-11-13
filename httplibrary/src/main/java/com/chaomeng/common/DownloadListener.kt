package com.chaomeng.common

interface DownloadListener {

    fun onDownloadIng(progressValue: Long, maxValue: Long)
    fun onError(msg: String)
    fun onComplete()
    fun onStart()
}