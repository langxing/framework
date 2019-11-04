package com.chaomeng.http

interface RequestManager<T: IRequest> {

    fun createRequest(): T

}