package com.chaomeng.androidframework.ui.test

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chaomeng.androidframework.R
import kotlinx.android.synthetic.main.activity_welecome.*

@SuppressLint("Registered")
class WelecomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welecome)
        tvOkHTTP.setOnClickListener {
            val intent = Intent(this, OkhttpActivity::class.java)
            startActivity(intent)
        }
        tvRetrofit.setOnClickListener {
            val intent = Intent(this, RetrofitActivity::class.java)
            startActivity(intent)
        }
    }
}