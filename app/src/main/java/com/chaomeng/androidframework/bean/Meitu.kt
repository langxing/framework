package com.chaomeng.androidframework.bean

data class Meitu(val createdAt: String,
                 val publishedAt: String,
                 val url: String) {

    override fun toString(): String {
        return "createdAt=$createdAt,publishedAt=$publishedAt,url=$url"
    }
}