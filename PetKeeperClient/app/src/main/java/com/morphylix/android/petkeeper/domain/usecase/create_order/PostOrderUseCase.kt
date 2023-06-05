package com.morphylix.android.petkeeper.domain.usecase.create_order

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.domain.Order
import javax.inject.Inject

class PostOrderUseCase @Inject constructor(
    private val petKeeperRepository: PetKeeperRepository
) {

    suspend fun execute(order: Order): Order {
        return petKeeperRepository.postOrder(order)
    }

}