package com.chaomeng.androidframework.ui.test

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaomeng.androidframework.R
import com.chaomeng.androidframework.RequestManager
import com.chaomeng.androidframework.bean.MusicStation
import com.chaomeng.androidframework.http.BaseResponse
import com.chaomeng.common.DownloadListener
import com.chaomeng.androidframework.http.HttpResponse
import com.chaomeng.http.AbsResponse
import com.chaomeng.http.UploadListener
import com.hjq.permissions.OnPermission
import com.hjq.permissions.XXPermissions
import kotlinx.android.synthetic.main.activity_okhttp.*
import okhttp3.MediaType
import java.io.File
import java.math.BigDecimal

class OkhttpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_okhttp)
        RequestManager.get().getRequest()
            .setType<BaseResponse<List<MusicStation>>>()
            .get("https://api.apiopen.top/musicBroadcasting", response = object: HttpResponse<BaseResponse<List<MusicStation>>>() {

                override fun onSuccess(data: BaseResponse<List<MusicStation>>?) {
                    tvMusic.text = "${data?.result?.get(0)?.channellist?.get(0)?.thumb}"
                }
            })
        tvDownload.setOnClickListener {
            if (XXPermissions.isHasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                download()
            } else {
                XXPermissions.with(this)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request(object: OnPermission {
                        override fun noPermission(denied: MutableList<String>?, quick: Boolean) {

                        }

                        override fun hasPermission(granted: MutableList<String>?, isAll: Boolean) {
                            download()
                        }

                    })
            }

        }
        tvUpload.setOnClickListener {
            if (XXPermissions.isHasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                upload()
            } else {
                XXPermissions.with(this)
                    .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .request(object: OnPermission {
                        override fun noPermission(denied: MutableList<String>?, quick: Boolean) {

                        }

                        override fun hasPermission(granted: MutableList<String>?, isAll: Boolean) {
                            upload()
                        }

                    })
            }
        }
    }

    private fun download() {
        val path = "data/data/$packageName/cmvip.apk"
        val file = File(path)
        if (!file.exists()) {
            file.createNewFile()
        }
        RequestManager.get().getRequest()
            .download("http://oss.pgyer.com/df1e7fee335874fb6a7098de88613dee.apk?auth_key=1572487826-844bb491e794c7bd1545ff53fa73e4a6-0-fdf7e69ac83e201ce2e9edec218cf21e&response-content-disposition=attachment%3B+filename%3Dcmvip-prod-release-v1.1.0-110-7453f9c1.apk",
                file, object: DownloadListener {
                    override fun onDownloadIng(progressValue: Long, maxValue: Long) {
                        Log.e("progress", BigDecimal(progressValue.toString()).divide(BigDecimal(maxValue.toString()), 2, BigDecimal.ROUND_HALF_UP).toPlainString())
                    }

                    override fun onError(msg: String) {
                        Toast.makeText(this@OkhttpActivity, msg, Toast.LENGTH_SHORT).show()
                    }

                    override fun onComplete() {
                        Toast.makeText(this@OkhttpActivity, "下载完成", Toast.LENGTH_SHORT).show()
                    }

                    override fun onStart() {
                        Toast.makeText(this@OkhttpActivity, "开始下载", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun upload() {
        val path = "data/data/$packageName/cmvip.apk"
        val file = File(path)
        if (!file.exists()) {
            file.createNewFile()
        }
        val mediaType = MediaType.parse("application/octet-stream")
        RequestManager.get().getRequest()
            .upload("http://t.xinhuo.com/index.php/Api/Pic/uploadPic", file, mediaType!!, object:
                UploadListener {
                override fun onUploadIng(progressValue: Long, maxValue: Long) {
                    Log.e("progress", BigDecimal(progressValue.toString()).divide(BigDecimal(maxValue.toString()), 2, BigDecimal.ROUND_HALF_UP).toPlainString())
                }

                override fun onError(msg: String) {
                    Toast.makeText(this@OkhttpActivity, msg, Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {
                    Toast.makeText(this@OkhttpActivity, "上传完成", Toast.LENGTH_SHORT).show()
                }

                override fun onStart() {
                    Toast.makeText(this@OkhttpActivity, "开始上传", Toast.LENGTH_SHORT).show()
                }

            })
    }

}
