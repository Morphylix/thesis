package com.morphylix.android.petkeeper.domain.usecase.notification_list

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.domain.Notification
import javax.inject.Inject

class FetchUserNotificationsUseCase @Inject constructor(
    private val petKeeperRepository: PetKeeperRepository
) {

    suspend fun execute(email: String): List<Notification> {
        return petKeeperRepository.fetchUserNotifications(email)
    }

}