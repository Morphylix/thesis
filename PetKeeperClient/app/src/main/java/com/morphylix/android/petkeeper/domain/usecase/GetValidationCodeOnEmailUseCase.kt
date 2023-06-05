package com.morphylix.android.petkeeper.domain.usecase

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.network.validate_email.UserValidateEmailDto
import javax.inject.Inject

class GetValidationCodeOnEmailUseCase
    @Inject constructor(private val petKeeperRepository: PetKeeperRepository) {

        suspend fun execute(email: String) {
            val userValidateEmailDto = UserValidateEmailDto(email)
            petKeeperRepository.getValidationCodeOnEmail(userValidateEmailDto)
        }
}