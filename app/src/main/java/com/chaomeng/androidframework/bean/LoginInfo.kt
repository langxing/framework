package com.chaomeng.androidframework.bean

import java.io.Serializable

data class LoginInfo(val key: String,
                     val phone: String,
                     val publicName: String,
                     val passwd: String,
                     val text: String,
                     val img: String,
                     val createTime: String) : Serializable {

    override fun toString(): String {
        return "key=$key,phone=$phone,publicName=$publicName,password=$passwd,text=$text,img=$img,time=$createTime"
    }
}