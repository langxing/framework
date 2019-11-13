package com.chaomeng.androidframework.bean

data class SystemBean(val children: List<SystemItem>,
                      val name: String,
                      val id: String) {

    data class SystemItem(val name: String,
                          val id: String)
}