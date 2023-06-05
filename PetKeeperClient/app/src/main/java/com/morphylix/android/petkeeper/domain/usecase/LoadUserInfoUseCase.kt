package com.morphylix.android.petkeeper.domain.usecase

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.domain.User
import com.morphylix.android.petkeeper.domain.model.network.load_user_info.UserInfoDto
import javax.inject.Inject

class LoadUserInfoUseCase
@Inject constructor(val petKeeperRepository: PetKeeperRepository) {

    suspend fun execute(): User {
        val token = petKeeperRepository.getCurrentToken()
        val userInfoDto = UserInfoDto(token!!)
        return petKeeperRepository.loadUserInfo(userInfoDto)
    }
}