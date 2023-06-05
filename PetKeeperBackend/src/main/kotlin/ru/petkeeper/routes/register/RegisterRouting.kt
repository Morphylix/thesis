package ru.petkeeper.routes.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.token_table.TokenDto
import ru.petkeeper.database.token_table.TokensTable
import ru.petkeeper.database.users_table.UsersDto
import ru.petkeeper.database.users_table.UsersTable
import ru.petkeeper.util.sha256Hash
import java.util.*

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            val receive = call.receive(RegisterReceiveRemote::class)

            val user = UsersTable.fetchUser(receive.login)
            println(user?.password)

            if (user != null) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            } else {
                val token = UUID.randomUUID().toString()

                TokensTable.insertToken(
                    TokenDto(
                        token,
                        receive.login
                    )
                )

                UsersTable.insertUser(
                    UsersDto(
                        receive.login,
                        sha256Hash(receive.password),
                        receive.name,
                        receive.surname,
                        ""
                    )
                )
                call.respond(RegisterResponseRemote(token))
            }
        }
    }
}