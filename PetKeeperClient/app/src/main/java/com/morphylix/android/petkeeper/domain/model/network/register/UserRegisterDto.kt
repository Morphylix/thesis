package com.morphylix.android.petkeeper.domain.model.network.register

data class UserRegisterDto(
    val login: String,
    val password: String,
    val name: String,
    val surname: String = ""
)