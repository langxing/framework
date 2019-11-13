package com.chaomeng.androidframework.bean

data class ArticleBean(val datas: List<Article>) {

    data class Article(val author: String,
                       val collect: Boolean,
                       val chapterName: String,
                       val link: String,
                       val niceDate: String,
                       val title: String,
                       val superChapterName: String)
}