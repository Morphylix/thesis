package com.morphylix.android.petkeeper.domain.usecase.user_profile

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.domain.User
import javax.inject.Inject

class FetchUserByEmailUseCase @Inject constructor(
    private val petKeeperRepository: PetKeeperRepository
) {

    suspend fun execute(email: String): User {
        return petKeeperRepository.fetchUserByEmail(email)
    }

}