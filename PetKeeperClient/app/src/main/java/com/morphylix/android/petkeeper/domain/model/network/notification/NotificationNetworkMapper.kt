package com.morphylix.android.petkeeper.domain.model.network.notification

import com.morphylix.android.petkeeper.domain.model.domain.Notification
import com.morphylix.android.petkeeper.util.EntityMapper
import javax.inject.Inject

class NotificationNetworkMapper @Inject constructor(): EntityMapper<NotificationNetworkEntity, Notification>() {
    override fun mapFromEntity(entity: NotificationNetworkEntity): Notification {
        return Notification(
            id = entity.id,
            senderEmail = entity.senderEmail,
            receiverEmail = entity.receiverEmail,
            body = entity.body
        )
    }

    override fun mapToEntity(model: Notification): NotificationNetworkEntity {
        return NotificationNetworkEntity(
            id = model.id,
            senderEmail = model.senderEmail,
            receiverEmail = model.receiverEmail,
            body = model.body
        )
    }


}