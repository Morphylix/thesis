package ru.petkeeper.routes.post_notification

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.notifications_table.NotificationDto
import ru.petkeeper.database.notifications_table.NotificationsTable

fun Application.configurePostNotificationRouting() {
    routing {
        post("create_notification") {
            val notificationDto = call.receive(NotificationDto::class)

            call.respond(NotificationsTable.insertNotification(notificationDto))
        }
    }
}