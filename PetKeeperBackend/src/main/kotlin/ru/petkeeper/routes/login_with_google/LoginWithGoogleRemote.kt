package ru.petkeeper.routes.login_with_google

data class LoginWithGoogleReceiveRemote(
    val login: String,
    val password: String,
    val name: String
)

data class LoginWithGoogleResponseRemote(
    val token: String
)