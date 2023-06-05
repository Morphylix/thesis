package com.morphylix.android.petkeeper.util.serializers

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.morphylix.android.petkeeper.domain.model.network.pet.PetNetworkEntity
import java.lang.reflect.Type

private const val TAG = "PetDeserializer"

class PetDeserializer : JsonDeserializer<PetNetworkEntity> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): PetNetworkEntity {
        val jsonObject = json.asJsonObject
        Log.i(TAG, "deserializing picture")
        val picBytes =
            context.deserialize<ByteArray>(jsonObject.get("picture"), ByteArray::class.java)
        Log.i(TAG, "pic bytes is $picBytes")

        return PetNetworkEntity(picture = picBytes)
    }
}
