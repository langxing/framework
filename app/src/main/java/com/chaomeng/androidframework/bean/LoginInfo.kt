package com.chaomeng.androidframework.bean

data class LoginInfo(val key: String,
                     val phone: String,
                     val name: String,
                     val passwd: String,
                     val text: String,
                     val img: String,
                     val createTime: String) {

    override fun toString(): String {
        return "key=$key,phone=$phone,name=$name,password=$passwd,text=$text,img=$img,time=$createTime"
    }
}