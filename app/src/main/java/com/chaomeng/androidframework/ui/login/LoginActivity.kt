package com.chaomeng.androidframework.ui.login

import android.os.Bundle
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chaomeng.androidframework.R
import com.chaomeng.androidframework.base.BaseActivity
import com.chaomeng.androidframework.bean.LoginInfo
import com.chaomeng.androidframework.common.Constant
import com.chaomeng.androidframework.common.ViewModelFactory
import com.chaomeng.androidframework.utils.CacheManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private val model: LoginViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(this)).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener {
            model.login(editAccount.text.toString().trim(),
                editPassword.text.toString().trim())
        }
        model.cookie.observe(this, Observer<String> {
            CacheManager.get().putString(Constant.KEY_COOKIE, it).commit()
        })
        model.loginInfo.observe(this, Observer<LoginInfo> {
            CacheManager.get().putObject(Constant.KEY_LOGININFO, it)
            finish()
        })
    }
}