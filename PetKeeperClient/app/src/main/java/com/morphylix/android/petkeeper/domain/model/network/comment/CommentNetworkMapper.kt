package com.morphylix.android.petkeeper.domain.model.network.comment

import com.morphylix.android.petkeeper.domain.model.domain.Comment
import com.morphylix.android.petkeeper.util.EntityMapper
import javax.inject.Inject

class CommentNetworkMapper @Inject constructor(): EntityMapper<CommentNetworkEntity, Comment>() {
    override fun mapFromEntity(entity: CommentNetworkEntity): Comment {
        return Comment(
            id = entity.id,
            senderEmail = entity.senderEmail,
            receiverEmail = entity.receiverEmail,
            body = entity.body,
            rating = entity.rating
        )
    }

    override fun mapToEntity(model: Comment): CommentNetworkEntity {
        return CommentNetworkEntity(
            id = model.id,
            senderEmail = model.senderEmail,
            receiverEmail = model.receiverEmail,
            body = model.body,
            rating = model.rating
        )
    }


}