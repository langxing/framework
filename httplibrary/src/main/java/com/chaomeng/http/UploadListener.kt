package com.chaomeng.http

import com.chaomeng.common.DownloadListener

interface UploadListener : DownloadListener {

    override fun onDownloadIng(progressValue: Long, maxValue: Long) {

    }

    fun onUploadIng(progressValue: Long, maxValue: Long)
}