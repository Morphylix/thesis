package ru.petkeeper.database.token_table

data class TokenDto(
    val token: String,
    val email: String = ""
)
