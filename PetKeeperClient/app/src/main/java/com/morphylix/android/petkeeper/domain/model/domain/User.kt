package com.morphylix.android.petkeeper.domain.model.domain

data class User(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val surname: String = "",
    val phone: String = "",
    val rating: Double = -1.0,
    val totalVotes: Int = -1
)