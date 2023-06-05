package ru.petkeeper.database.notifications_table

data class NotificationDto(
    val id: Int,
    val senderEmail: String,
    val receiverEmail: String,
    val body: String
    )
