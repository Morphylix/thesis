package com.morphylix.android.petkeeper.domain.model.network.notification

data class NotificationNetworkEntity(
    val id: Int,
    val senderEmail: String,
    val receiverEmail: String,
    val body: String
)