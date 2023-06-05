package ru.petkeeper.routes.login_with_google

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.token_table.TokenDto
import ru.petkeeper.database.token_table.TokensTable
import ru.petkeeper.database.users_table.UsersDto
import ru.petkeeper.database.users_table.UsersTable
import ru.petkeeper.routes.login.LoginResponseRemote
import ru.petkeeper.util.sha256Hash
import java.util.*

fun Application.configureLoginWithGoogleRouting() {
    routing {
        post("/login_with_google") {
            val receive = call.receive(LoginWithGoogleReceiveRemote::class)

            println("RECEIVE IS $receive")

            val user = UsersTable.fetchUser(receive.login)

            if (user == null || user.password != sha256Hash(receive.password)) {
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
                        "",
                        ""
                    )
                )
                call.respond(LoginResponseRemote(token))
            } else {
                val token = UUID.randomUUID().toString()

                TokensTable.insertToken(
                    TokenDto(
                        token,
                        receive.login
                    )
                )

                call.respond(LoginResponseRemote(token))
            }
        }
    }
}