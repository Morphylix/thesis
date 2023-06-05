package com.morphylix.android.petkeeper.domain.model.network.order

import com.morphylix.android.petkeeper.domain.model.domain.Pet
import com.morphylix.android.petkeeper.domain.model.domain.User
import java.util.*

data class OrderNetworkEntity(
    val orderId: Int = -1,
    val pet: Pet? = Pet(),
    val createDate: Date = Date(25352525236),
    val isCancelled: Boolean = false,
    val recs: String = "",
    val about: String = "",
    val userEmail: String = "",
    val isTemporary: Boolean = false,
    val startDate: Date? = null,
    val endDate: Date? = null
)
