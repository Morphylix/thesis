package com.morphylix.android.petkeeper.domain.usecase

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import javax.inject.Inject

class SaveTokenUseCase
    @Inject constructor(private val petKeeperRepository: PetKeeperRepository) {

        fun execute(token: String) {
            petKeeperRepository.saveToken(token)
        }
}