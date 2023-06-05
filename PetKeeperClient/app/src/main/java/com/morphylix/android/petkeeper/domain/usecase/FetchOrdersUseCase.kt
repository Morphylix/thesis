package com.morphylix.android.petkeeper.domain.usecase

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.domain.Order
import com.morphylix.android.petkeeper.domain.model.domain.settings.SettingsConfig
import javax.inject.Inject

class FetchOrdersUseCase @Inject constructor(
    private val petKeeperRepository: PetKeeperRepository
) {

    suspend fun execute(): List<Order> { // TODO: ADD FILTERS?
        val settingsConfig = petKeeperRepository.getSettingsConfig()
        return petKeeperRepository.fetchOrders(settingsConfig)
    }

}