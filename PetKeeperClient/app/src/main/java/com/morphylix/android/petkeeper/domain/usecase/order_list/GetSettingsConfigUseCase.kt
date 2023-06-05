package com.morphylix.android.petkeeper.domain.usecase.order_list

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.domain.settings.SettingsConfig
import javax.inject.Inject

class GetSettingsConfigUseCase @Inject constructor(
    private val petKeeperRepository: PetKeeperRepository
){

    fun execute(): SettingsConfig {
        return petKeeperRepository.getSettingsConfig()
    }

}