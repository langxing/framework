package com.chaomeng.androidframework.bean

data class ProjectBean(val datas: List<Project>) {

    data class Project(val author: String,
                       val desc: String,
                       val title: String,
                       val envelopePic: String,
                       val link: String,
                       val publishTime: String,
                       val collect: Boolean)
}