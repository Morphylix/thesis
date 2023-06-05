package com.morphylix.android.petkeeper.domain.usecase.order_details

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import javax.inject.Inject

class CloseOrderUseCase @Inject constructor(
    private val petKeeperRepository: PetKeeperRepository
) {

    suspend fun execute(orderId: Int) {
        petKeeperRepository.closeOrder(orderId)
    }

}