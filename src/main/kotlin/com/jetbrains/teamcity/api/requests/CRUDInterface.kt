package com.jetbrains.teamcity.api.requests

import com.jetbrains.teamcity.api.models.BaseModel

interface CRUDInterface {
    fun create(model: BaseModel): Any
    fun read(id: String): Any
    fun update(id: String, model: BaseModel): Any
    fun delete(id: String): Any
}
