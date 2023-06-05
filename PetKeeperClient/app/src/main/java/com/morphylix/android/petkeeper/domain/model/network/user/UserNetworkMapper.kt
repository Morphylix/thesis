package com.morphylix.android.petkeeper.domain.model.network.user

import com.morphylix.android.petkeeper.domain.model.domain.User
import com.morphylix.android.petkeeper.util.EntityMapper
import com.morphylix.android.petkeeper.domain.model.network.load_user_info.UserNetworkEntity
import javax.inject.Inject

class UserNetworkMapper @Inject constructor(): EntityMapper<UserNetworkEntity, User>() {
    override fun mapFromEntity(entity: UserNetworkEntity): User {
        return User(
            email = entity.email,
            password = entity.password,
            name = entity.name,
            surname = entity.surname,
            phone = entity.phone,
            rating = entity.rating,
            totalVotes = entity.totalVotes
        )
    }

    override fun mapToEntity(model: User): UserNetworkEntity {
       return UserNetworkEntity(
           email = model.email,
           password = model.password,
           name = model.name,
           surname = model.surname,
           phone = model.phone,
           rating = model.rating,
           totalVotes = model.totalVotes
       )
    }
}