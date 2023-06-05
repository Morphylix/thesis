package com.morphylix.android.petkeeper.domain.model.domain

import java.util.*

data class Order(
    val orderId: Int = -1,
    var pet: Pet? = Pet(),
    val createDate: Date = Date(25352525236),
    val isCancelled: Boolean = false,
    val recs: String = "",
    val about: String = "",
    var user: User? = User(),
    val isTemporary: Boolean = false,
    val startDate: Date? = null,
    val endDate: Date? = null
    )
