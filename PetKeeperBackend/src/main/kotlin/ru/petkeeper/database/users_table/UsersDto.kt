package ru.petkeeper.database.users_table

data class UsersDto(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val surname: String = "",
    val phone: String = "",
    val rating: Double = 0.0,
    val totalVotes: Int = 0
)
