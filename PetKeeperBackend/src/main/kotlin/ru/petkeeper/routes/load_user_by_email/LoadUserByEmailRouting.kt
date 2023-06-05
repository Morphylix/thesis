package ru.petkeeper.routes.load_user_by_email

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.users_table.UsersTable

fun Application.configureLoadUserByEmailRouting() {
    routing {
        get("/load_user_by_email") {
            val email = call.request.queryParameters["email"]
            val user = UsersTable.fetchUser(email ?: "")
            if (user != null) {
                call.respond(user)
            }
        }
    }
}