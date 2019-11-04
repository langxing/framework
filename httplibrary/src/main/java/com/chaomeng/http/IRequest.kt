package com.chaomeng.http

import okhttp3.MediaType
import java.io.File

interface IRequest {

    fun <T> put(url: String, response: IResponse<T>)

    fun <T> get(url: String, response: IResponse<T>)

    fun <T> post(url: String, response: IResponse<T>)

    fun <T> delete(url: String, response: IResponse<T>)

    fun upload(url: String, file: File, mediaType: MediaType, listener: UploadListener)

    fun download(url: String, file: File, listener: DownloadListener)

    fun addHeader(key: String, value: Any): IRequest

    fun addParam(key: String, value: Any): IRequest

    fun setTag(requestTag: String): IRequest

    fun cancel(requestTag: String)
}