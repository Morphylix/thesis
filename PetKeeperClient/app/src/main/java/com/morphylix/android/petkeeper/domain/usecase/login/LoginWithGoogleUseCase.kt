package com.morphylix.android.petkeeper.domain.usecase.login

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.network.login.UserLoginDto
import com.morphylix.android.petkeeper.domain.model.network.register.UserRegisterDto
import com.morphylix.android.petkeeper.util.sha256Hash
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val petKeeperRepository: PetKeeperRepository
) {

    suspend fun execute(email: String, password: String, name: String): String {
        val userRegisterDto = UserRegisterDto(email, sha256Hash(password), name)
        return petKeeperRepository.loginWithGoogleAndFetchToken(userRegisterDto).token
    }

}