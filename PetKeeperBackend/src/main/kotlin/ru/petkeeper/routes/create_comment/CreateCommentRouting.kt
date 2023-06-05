package ru.petkeeper.routes.create_comment

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.comments_table.CommentDto
import ru.petkeeper.database.comments_table.CommentsTable
import ru.petkeeper.database.users_table.UsersTable

fun Application.configureCreateCommentRouting() {
    routing {
        post("/create_comment") {
            val comment = call.receive(CommentDto::class)

            val exists = CommentsTable.checkCommentAlreadyCreated(comment.senderEmail, comment.receiverEmail)

            if (exists) {
                call.respond("Such comment already exists")
            } else {
                CommentsTable.insertComment(comment)
                UsersTable.updateRating(comment.rating, comment.receiverEmail)
                call.respond("ok")
            }
        }
    }
}