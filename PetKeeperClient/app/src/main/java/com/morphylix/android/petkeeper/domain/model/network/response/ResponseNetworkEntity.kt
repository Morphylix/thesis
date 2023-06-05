package com.morphylix.android.petkeeper.domain.model.network.response

data class ResponseNetworkEntity(
    val id: Int,
    val userEmail: String,
    val orderId: Int,
    val body: String,
    val status: Boolean
)
