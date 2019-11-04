package com.chaomeng.retrofit

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class TaskCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Task<T>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<T>): Task<T> {
        return Task(call)
    }

}