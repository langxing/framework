package com.chaomeng.jsondeserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.Exception
import java.lang.reflect.Type

class LongJsonDeserializer : JsonDeserializer<Long> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Long {
        return try {
            json?.asLong ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
}