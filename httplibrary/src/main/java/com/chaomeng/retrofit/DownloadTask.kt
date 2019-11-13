package com.chaomeng.retrofit

import android.os.Handler
import android.os.Looper
import com.chaomeng.common.DownloadListener
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class DownloadTask(private val call: Call<ResponseBody>) {

    private val handler = Handler(Looper.getMainLooper())

    fun execute(file: File, listener: DownloadListener? = null) {
        handler.post {
            listener?.onStart()
        }
        call.enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                response.body()?.let { it ->
                    var inputStream: InputStream? = null
                    var outputStream: OutputStream? = null
                    try {
                        if (!file.exists()) {
                            file.parentFile.mkdirs()
                            file.createNewFile()
                        }
                        inputStream = it.byteStream()
                        outputStream = FileOutputStream(file)
                        val totalSize = it.contentLength()
                        val array = ByteArray(2048)
                        var progressSize = 0L
                        var len: Int
                        while (inputStream.read(array).also { count -> len = count } != -1) {
                            outputStream.write(array, 0, len)
                            progressSize += len
                            listener?.onDownloadIng(progressSize, totalSize)
                        }
                        listener?.onComplete()
                        inputStream.close()
                        outputStream.flush()
                        outputStream.close()
                    } catch (e: IOException) {
                        listener?.onError("${e.message}")
                    } finally {
                        inputStream?.close()
                        outputStream?.close()
                    }
                }
            }

        })
    }
}