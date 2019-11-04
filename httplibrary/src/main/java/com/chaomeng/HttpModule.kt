package com.chaomeng

import android.app.Application

class HttpModule {

    private lateinit var application: Application

    companion object {
        val newInstance: HttpModule by lazy {
            HttpModule()
        }
    }

    fun init(app: Application) {
        application = app
    }

    fun getApplication(): Application {
        return application
    }
}