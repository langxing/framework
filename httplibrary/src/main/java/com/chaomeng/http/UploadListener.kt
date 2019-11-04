package com.chaomeng.http

interface UploadListener : DownloadListener {

    override fun onDownloadIng(progressValue: Long, maxValue: Long) {

    }

    fun onUploadIng(progressValue: Long, maxValue: Long)
}