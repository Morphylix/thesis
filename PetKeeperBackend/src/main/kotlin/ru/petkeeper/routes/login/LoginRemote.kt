package ru.petkeeper.routes.login

data class LoginReceiveRemote(
    val email: String,
    val password: String
)

data class LoginResponseRemote(
    val token: String
)