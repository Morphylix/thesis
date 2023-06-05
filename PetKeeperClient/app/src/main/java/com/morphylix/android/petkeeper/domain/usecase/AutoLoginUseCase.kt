package com.morphylix.android.petkeeper.domain.usecase

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.network.auto_login.UserAutoLoginDto
import javax.inject.Inject

class AutoLoginUseCase
    @Inject constructor(private val petKeeperRepository: PetKeeperRepository) {

        suspend fun execute(): Boolean {
            val token = petKeeperRepository.getCurrentToken() ?: return false
            val userAutoLoginDto = UserAutoLoginDto(token)
            return petKeeperRepository.autoLoginWithToken(userAutoLoginDto).isAllowed
        }
}