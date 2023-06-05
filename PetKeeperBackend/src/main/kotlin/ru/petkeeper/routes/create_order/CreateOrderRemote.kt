package ru.petkeeper.routes.create_order

import ru.petkeeper.database.pets_table.PetDto
import java.util.*

data class CreateOrderReceiveRemote(
    val pet: PetDto = PetDto(),
    val createDate: Date = Date(25352525236),
    val isCancelled: Boolean = false,
    val recs: String = "",
    val about: String = "",
    val isTemporary: Boolean = false,
    val startDate: Date? = null,
    val endDate: Date? = null
)

data class CreateOrderResponseRemote(
    val pet: PetDto = PetDto(),
    val createDate: Date = Date(25352525236),
    val isCancelled: Boolean = false,
    val recs: String = "",
    val about: String = "",
    val isTemporary: Boolean = false,
    val startDate: Date? = null,
    val endDate: Date? = null
)