package com.chaomeng.retrofit

import com.chaomeng.common.DownloadListener
import com.chaomeng.common.HttpCode
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import java.io.*

class DownloadObserver(private val file: File, private var listener: DownloadListener? = null) : AbsObserver<ResponseBody> {

    override fun onSuccess(response: ResponseBody?) {
        response?.let {
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                if (!file.exists()) {
                    file.parentFile.mkdirs()
                    file.createNewFile()
                }
                inputStream = response.byteStream()
                outputStream = FileOutputStream(file)
                val totalSize = response.contentLength()
                val array = ByteArray(2048)
                var progressSize = 0L
                var len: Int
                while (inputStream.read(array).also { len = it } != -1) {
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

    override fun onError(httpCode: HttpCode, businessCode: String?, error: String?) {

    }

    override fun onError(e: Throwable) {
        listener?.onError("${e.message}")
    }

    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {
        listener?.onStart()
    }

    override fun onNext(t: ResponseBody) {

    }
    
    
}