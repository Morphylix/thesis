package com.morphylix.android.petkeeper.domain.model.domain

data class Response(
    val id: Int,
    val userEmail: String,
    val orderId: Int,
    val body: String,
    val status: Boolean
)