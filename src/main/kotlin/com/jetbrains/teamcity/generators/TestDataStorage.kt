package com.jetbrains.teamcity.generators

import com.jetbrains.teamcity.enums.Endpoint
import com.jetbrains.teamcity.models.BaseModel
import com.jetbrains.teamcity.requests.unchecked.UncheckedRequests
import com.jetbrains.teamcity.spec.Specification
import java.util.EnumMap

object TestDataStorage {

    private val createdEntities = EnumMap<Endpoint, MutableSet<String>>(Endpoint::class.java)

    fun addCreatedEntity(endpoint: Endpoint, baseModel: BaseModel) {
        addCreatedEntity(endpoint, getEntityIdOrLocator(baseModel))
    }

    fun deleteEntities() {
        createdEntities.forEach { endpoint, ids ->
            ids.forEach { id ->
                UncheckedRequests(Specification.superUserSpec()).getRequest(endpoint).delete(id)
            }
        }
        createdEntities.clear()
    }

    private fun addCreatedEntity(endpoint: Endpoint, id: String) {
        createdEntities.computeIfAbsent(endpoint) { mutableSetOf() }.add(id)
    }

    fun getEntityIdOrLocator(model: BaseModel): String {
        return try {
            model::class.java.getDeclaredField("id").let {
                it.isAccessible = true
                it.get(model)?.toString()
            } ?: model::class.java.getDeclaredField("locator").let {
                it.isAccessible = true
                it.get(model)?.toString()
            }!!
        } catch (e: Exception) {
            throw IllegalStateException("Cannot get id or locator of entity", e)
        }
    }
}
