package ru.petkeeper.routes.load_user_info

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.token_table.TokensTable
import ru.petkeeper.database.users_table.UsersTable

fun Application.configureLoadUserInfoRouting() {
    routing {
        post("/load_user_info") {
            val receive = call.receive(LoadUserInfoReceiveRemote::class)

            val email = TokensTable.fetchEmail(receive.token)
            val user = UsersTable.fetchUser(email ?: "")

            if (user != null) {
                call.respond(user)
            }
        }
    }
}