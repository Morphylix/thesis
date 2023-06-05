package ru.petkeeper.routes.fetch_user_notifications

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.notifications_table.NotificationsTable

fun Application.configureFetchUserNotificationsRouting() {
    routing {
        get("/load_user_notifications") {
            val email = call.request.queryParameters["email"]
            if (email != null) {
                call.respond(NotificationsTable.fetchNotificationsByEmail(email))
            }
        }
    }
}