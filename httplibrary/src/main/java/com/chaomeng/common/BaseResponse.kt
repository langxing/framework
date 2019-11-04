package com.chaomeng.common

data class BaseResponse<T>(val code: String, val data: T, val msg: String) {
}