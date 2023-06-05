package com.morphylix.android.petkeeper.domain.usecase

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.network.submit_validation.UserSubmitValidationDto
import javax.inject.Inject

class SubmitValidationCodeUseCase
    @Inject constructor(private val petKeeperRepository: PetKeeperRepository) {

        suspend fun execute(email: String, code: String){
            val userSubmitValidationDto = UserSubmitValidationDto(email, code)
            petKeeperRepository.submitValidationCode(userSubmitValidationDto)
        }
}