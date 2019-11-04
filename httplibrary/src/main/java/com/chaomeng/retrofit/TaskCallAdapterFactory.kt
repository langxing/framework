package com.chaomeng.retrofit

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class TaskCallAdapterFactory private constructor() : CallAdapter.Factory() {

    companion object {
        private val instance: TaskCallAdapterFactory by lazy {
            TaskCallAdapterFactory()
        }

        fun get() = instance
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        if (rawType != Task::class.java && Task::class.java.isAssignableFrom(rawType)) {
            return null
        }
        if (returnType !is ParameterizedType) {
            throw IllegalArgumentException(
                "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>"
            )
        }
        val responseType = getParameterUpperBound(0, returnType)

        return TaskCallAdapter<Any>(responseType)
    }

}