package com.morphylix.android.petkeeper.domain.model.domain

data class Notification(
    val id: Int,
    val senderEmail: String,
    var senderName: String = "",
    var senderSurname: String = "",
    val receiverEmail: String,
    val body: String
)
