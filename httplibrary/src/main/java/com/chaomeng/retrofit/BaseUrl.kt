package com.chaomeng.retrofit

class BaseUrl {

    var defaultUrl: String = ""
    var baseUrl: String = ""
    var useDefaultUrl = true

    companion object {
        private val instance: BaseUrl by lazy {
            BaseUrl()
        }

        fun get() = instance
    }

}