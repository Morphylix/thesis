package ru.petkeeper.routes.auto_login

data class AutoLoginReceiveRemote(
    val token: String
)

data class AutoLoginResponseRemote(
    val isAllowed: Boolean
)