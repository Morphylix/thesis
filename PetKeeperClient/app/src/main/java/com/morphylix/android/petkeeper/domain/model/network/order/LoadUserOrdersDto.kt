package com.morphylix.android.petkeeper.domain.model.network.order

import com.morphylix.android.petkeeper.presentation.user_profile.OrderType

data class LoadUserOrdersDto(
    val email: String,
    val type: OrderType
)
