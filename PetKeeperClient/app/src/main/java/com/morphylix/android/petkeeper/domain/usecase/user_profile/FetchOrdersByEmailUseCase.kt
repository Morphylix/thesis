package com.morphylix.android.petkeeper.domain.usecase.user_profile

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.domain.Order
import com.morphylix.android.petkeeper.domain.model.network.order.LoadUserOrdersDto
import com.morphylix.android.petkeeper.presentation.user_profile.OrderType
import javax.inject.Inject

class FetchOrdersByEmailUseCase @Inject constructor(
    private val petKeeperRepository: PetKeeperRepository
) {

    suspend fun execute(email: String, orderType: OrderType): List<Order> {
        val loadUserOrdersDto = LoadUserOrdersDto(email, orderType)
        return petKeeperRepository.fetchOrdersByEmail(loadUserOrdersDto)
    }

}