package com.morphylix.android.petkeeper.util

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "DateDeserializer"

class DateDeserializer: JsonDeserializer<Date> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Date {
        val date = json.asJsonPrimitive.asString
        Log.i(TAG, "Deserializing $date")
        Log.i(TAG, "deserialized result is ${SimpleDateFormat(DATE_PARSE_FORMAT, Locale.ENGLISH).parse(date)!!}")

        Log.i(TAG, " OR deserialized result is ${SimpleDateFormat(DATE_PARSE_FORMAT, Locale.ENGLISH).parse(date)!!}")
        return SimpleDateFormat(DATE_PARSE_FORMAT, Locale.ENGLISH).parse(date)!! // locale temp
    }

}