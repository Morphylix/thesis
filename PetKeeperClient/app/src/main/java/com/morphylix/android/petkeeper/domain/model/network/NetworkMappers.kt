package com.morphylix.android.petkeeper.domain.model.network

import com.morphylix.android.petkeeper.domain.model.network.comment.CommentNetworkMapper
import com.morphylix.android.petkeeper.domain.model.network.notification.NotificationNetworkMapper
import com.morphylix.android.petkeeper.domain.model.network.order.OrderNetworkMapper
import com.morphylix.android.petkeeper.domain.model.network.pet.PetNetworkMapper
import com.morphylix.android.petkeeper.domain.model.network.response.ResponseNetworkMapper
import com.morphylix.android.petkeeper.domain.model.network.user.UserNetworkMapper
import javax.inject.Inject

data class NetworkMappers @Inject constructor(
    val orderNetworkMapper: OrderNetworkMapper,
    val petNetworkMapper: PetNetworkMapper,
    val userNetworkMapper: UserNetworkMapper,
    val responseNetworkMapper: ResponseNetworkMapper,
    val commentNetworkMapper: CommentNetworkMapper,
    val notificationNetworkMapper: NotificationNetworkMapper
)