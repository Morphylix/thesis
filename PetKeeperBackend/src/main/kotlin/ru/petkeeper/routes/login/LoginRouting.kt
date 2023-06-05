package ru.petkeeper.routes.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.token_table.TokenDto
import ru.petkeeper.database.token_table.TokensTable
import ru.petkeeper.database.users_table.UsersTable
import java.util.*

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val receive = call.receive(LoginReceiveRemote::class)

            val user = UsersTable.fetchUser(receive.email)

            if (user == null || user.password != receive.password) {
                call.respond(HttpStatusCode.BadRequest, "User not found")
            } else {
                val token = UUID.randomUUID().toString()

                TokensTable.insertToken(
                    TokenDto(
                        token,
                        receive.email
                    )
                )

                call.respond(LoginResponseRemote(token))
            }
        }
    }
}