package com.morphylix.android.petkeeper.domain.model.network.load_user_info

data class UserNetworkEntity(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val phone: String,
    val rating: Double,
    val totalVotes: Int
)
