package com.chaomeng.androidframework.bean

import com.stx.xhb.androidx.entity.SimpleBannerInfo

data class Banner(val id: String, val imagePath: String) : SimpleBannerInfo() {

  override fun getXBannerUrl(): Any {
   return imagePath
  }

  override fun getXBannerTitle(): String {
   return ""
  }
}