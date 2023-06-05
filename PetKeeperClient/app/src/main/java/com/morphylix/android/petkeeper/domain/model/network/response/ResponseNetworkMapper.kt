package com.morphylix.android.petkeeper.domain.model.network.response

import com.morphylix.android.petkeeper.domain.model.domain.Response
import com.morphylix.android.petkeeper.util.EntityMapper
import javax.inject.Inject

class ResponseNetworkMapper @Inject constructor(): EntityMapper<ResponseNetworkEntity, Response>() {
    override fun mapFromEntity(entity: ResponseNetworkEntity): Response {
        return Response(
            id = entity.id,
            userEmail = entity.userEmail,
            orderId = entity.orderId,
            body = entity.body,
            status = entity.status
        )
    }

    override fun mapToEntity(model: Response): ResponseNetworkEntity {
        return ResponseNetworkEntity(
            id = model.id,
            userEmail = model.userEmail,
            orderId = model.orderId,
            body = model.body,
            status = model.status
        )
    }

}