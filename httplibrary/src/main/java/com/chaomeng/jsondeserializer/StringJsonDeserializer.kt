package com.chaomeng.jsondeserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.Exception
import java.lang.reflect.Type

class StringJsonDeserializer : JsonDeserializer<String> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): String {
        return try {
            json?.asString ?: ""
        } catch (e: Exception) {
           ""
        }
    }
}