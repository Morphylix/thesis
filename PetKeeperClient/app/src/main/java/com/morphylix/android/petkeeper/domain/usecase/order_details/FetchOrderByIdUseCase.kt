package com.morphylix.android.petkeeper.domain.usecase.order_details

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.domain.Order
import javax.inject.Inject

class FetchOrderByIdUseCase @Inject constructor(
    private val petKeeperRepository: PetKeeperRepository
) {

    suspend fun execute(orderId: Int): Order {
        return petKeeperRepository.fetchOrderById(orderId)
    }

}