package ru.petkeeper.database.orders_table

import ru.petkeeper.database.pets_table.PetDto
import java.util.*

data class OrderDto(
    val orderId: Int = -1,
    val pet: PetDto? = null,
    val createDate: Date = Date(25352525236),
    var isCancelled: Boolean = false,
    val recs: String = "",
    val about: String = "",
    val userEmail: String = "",
    val isTemporary: Boolean = false,
    val startDate: Date? = null,
    val endDate: Date? = null
)
