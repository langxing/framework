package com.chaomeng.jsondeserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.Exception
import java.lang.reflect.Type

class BooleanGsonDeserializer : JsonDeserializer<Boolean> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Boolean {
        return try {
            json?.asBoolean ?: false
        } catch (e: Exception) {
            false
        }
    }
}