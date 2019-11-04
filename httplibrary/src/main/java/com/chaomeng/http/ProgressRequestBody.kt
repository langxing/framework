package com.chaomeng.http

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.Okio
import java.io.File

class ProgressRequestBody(private val file: File, private val mediaType: MediaType, private val onUploadIng:((progressValue: Long, maxValue: Long) -> Unit)? = null) : RequestBody() {

    override fun contentLength(): Long {
        return file.length()
    }

    override fun contentType(): MediaType? {
        return mediaType
    }

    override fun writeTo(sink: BufferedSink) {
        val source = Okio.source(file)
        val max = contentLength()
        val buffer = Buffer()
        var current = 0L
        var len: Long
        while (source.read(buffer, 1024 * 8).also { len = it } != -1L) {
            sink.write(buffer, len)
            current += len
            onUploadIng?.invoke(current, max)
        }
    }
}