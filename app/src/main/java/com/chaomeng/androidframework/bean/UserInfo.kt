package com.chaomeng.androidframework.bean

data class UserInfo(val id: String,
                    val nickname: String,
                    val username: String,
                    val publicName: String) {

    override fun toString(): String {
        return "id=$id, nickname=$nickname, username= $username, publicName=$publicName"
    }
}