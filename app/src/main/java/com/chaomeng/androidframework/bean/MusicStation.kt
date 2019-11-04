package com.chaomeng.androidframework.bean

data class MusicStation(val channellist: List<Music>) {

    data class Music(val thumb: String,
                     val name: String)
}