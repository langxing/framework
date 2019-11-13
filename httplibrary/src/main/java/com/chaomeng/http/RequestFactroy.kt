package com.chaomeng.http

interface RequestFactroy<T: IRequest> {

    fun getRequest(): T

}