package ru.petkeeper.routes.auto_login

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.token_table.TokensTable

fun Application.configureAutoLoginRouting() {
    routing {
        post("/auto_login") {
            val receive = call.receive(AutoLoginReceiveRemote::class)

            val userToken = TokensTable.fetchToken(receive.token)

            if (userToken != null) {
                call.respond(AutoLoginResponseRemote(true))
            } else {
                call.respond(AutoLoginResponseRemote(false))
            }
        }
    }
}