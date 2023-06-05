package com.morphylix.android.petkeeper.util

abstract class EntityMapper<Entity, Model> {

    abstract fun mapFromEntity(entity: Entity): Model

    abstract fun mapToEntity(model: Model): Entity

    fun mapFromEntityList(entityList: List<Entity>): List<Model> {
        return entityList.map {
            mapFromEntity(it)
        }
    }

}