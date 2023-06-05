package com.morphylix.android.petkeeper.domain.usecase

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.network.login.UserLoginDto
import com.morphylix.android.petkeeper.util.sha256Hash
import javax.inject.Inject

class LoginUseCase
@Inject constructor(private val petKeeperRepository: PetKeeperRepository) {

    suspend fun execute(login: String, password: String): String {
        val userLoginDto = UserLoginDto(login, sha256Hash(password))
        return petKeeperRepository.loginAndFetchToken(userLoginDto).token
    }
}