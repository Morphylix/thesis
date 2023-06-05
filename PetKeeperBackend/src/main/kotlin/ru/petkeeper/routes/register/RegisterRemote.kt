package ru.petkeeper.routes.register

data class RegisterReceiveRemote(
    val login: String,
    val password: String,
    val name: String,
    val surname: String
)

data class RegisterResponseRemote(
    val token: String
)