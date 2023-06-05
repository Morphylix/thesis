package ru.petkeeper.database.notifications_table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.petkeeper.database.comments_table.CommentsTable
import ru.petkeeper.database.users_table.UsersTable

object NotificationsTable: Table("notifications") {
    var idCounter = 0
    val notificationId = NotificationsTable.integer("notificationId")
    private val senderEmail = NotificationsTable.varchar("senderEmail", 50).references(UsersTable.email)
    private val receiverEmail = NotificationsTable.varchar("receiverEmail", 50).references(UsersTable.email)
    private val body = NotificationsTable.text("body")

    fun insertNotification(notificationDto: NotificationDto): NotificationDto {
        transaction {
            val newId = NotificationsTable.selectAll().count().toInt()
            NotificationsTable.idCounter = newId
            NotificationsTable.insert {
                it[notificationId] = newId
                it[senderEmail] = notificationDto.senderEmail
                it[receiverEmail] = notificationDto.receiverEmail
                it[body] = notificationDto.body
            }
        }
        return notificationDto
    }

    fun fetchNotification(id: Int): NotificationDto? {
        return try {
            transaction {
                val commentModel = NotificationsTable.select {
                    NotificationsTable.notificationId.eq(id)
                }.single()
                NotificationDto(
                    id = commentModel[notificationId],
                    senderEmail = commentModel[receiverEmail],
                    receiverEmail = commentModel[receiverEmail],
                    body = commentModel[body],
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchNotificationsByEmail(email: String): List<NotificationDto> {
        return try {
            val resList = mutableListOf<NotificationDto>()
            transaction {
                val commentModelList = NotificationsTable.select {
                    NotificationsTable.receiverEmail.eq(email)
                }.toList()
                for (commentModel in commentModelList) {
                    resList.add(
                        NotificationDto(
                            id = commentModel[notificationId],
                            senderEmail = commentModel[senderEmail],
                            receiverEmail = commentModel[receiverEmail],
                            body = commentModel[body],
                        )
                    )
                }
            }
            resList
        } catch (e: Exception) {
            emptyList()
        }
    }
}