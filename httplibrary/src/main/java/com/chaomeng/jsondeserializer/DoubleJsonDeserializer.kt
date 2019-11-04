package com.chaomeng.jsondeserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.Exception
import java.lang.reflect.Type

class DoubleJsonDeserializer : JsonDeserializer<Double> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Double {
        return try {
            json?.asDouble ?: 0.00
        } catch (e: Exception) {
            0.00
        }
    }
}