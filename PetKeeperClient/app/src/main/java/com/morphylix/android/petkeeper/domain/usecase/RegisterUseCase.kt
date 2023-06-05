package com.morphylix.android.petkeeper.domain.usecase

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.network.register.UserRegisterDto
import com.morphylix.android.petkeeper.util.sha256Hash
import javax.inject.Inject

class RegisterUseCase
    @Inject constructor(private val petKeeperRepository: PetKeeperRepository) {

    suspend fun execute(login: String, password: String, name: String, surname: String): String {
        val userRegisterDto = UserRegisterDto(login, sha256Hash(password), name, surname)
        return petKeeperRepository.registerAndFetchToken(userRegisterDto).token
    }
}