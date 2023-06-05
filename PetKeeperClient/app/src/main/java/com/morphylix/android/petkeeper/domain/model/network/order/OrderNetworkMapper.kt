package com.morphylix.android.petkeeper.domain.model.network.order

import com.morphylix.android.petkeeper.domain.model.domain.Order
import com.morphylix.android.petkeeper.util.EntityMapper
import javax.inject.Inject

class OrderNetworkMapper @Inject constructor(): EntityMapper<OrderNetworkEntity, Order>() {
    override fun mapFromEntity(entity: OrderNetworkEntity): Order {
        return Order(
            orderId = entity.orderId,
            pet = entity.pet,
            createDate = entity.createDate,
            isCancelled = entity.isCancelled,
            recs = entity.recs,
            about = entity.about,
            isTemporary = entity.isTemporary,
            startDate = entity.startDate,
            endDate = entity.endDate
        )
    }

    override fun mapToEntity(model: Order): OrderNetworkEntity {
        return OrderNetworkEntity(
            orderId = model.orderId,
            pet = model.pet,
            createDate = model.createDate,
            isCancelled = model.isCancelled,
            recs = model.recs,
            about = model.about,
            isTemporary = model.isTemporary,
            startDate = model.startDate,
            endDate = model.endDate
        )
    }


}