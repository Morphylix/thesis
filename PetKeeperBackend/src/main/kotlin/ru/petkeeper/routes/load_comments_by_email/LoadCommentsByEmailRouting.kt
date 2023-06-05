package ru.petkeeper.routes.load_comments_by_email

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.comments_table.CommentsTable

fun Application.configureLoadCommentsByEmailRouting() {
    routing {
        get("/load_user_comments") {
            val email = call.request.queryParameters["email"]
            if (email != null) {
                call.respond(CommentsTable.fetchCommentsByEmail(email))
            }
        }
    }
}