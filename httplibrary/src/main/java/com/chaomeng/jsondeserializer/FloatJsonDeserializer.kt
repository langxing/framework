package com.chaomeng.jsondeserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.Exception
import java.lang.reflect.Type

class FloatJsonDeserializer : JsonDeserializer<Float> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Float {
        return try {
            json?.asFloat ?: 0f
        } catch (e: Exception) {
            0f
        }
    }
}