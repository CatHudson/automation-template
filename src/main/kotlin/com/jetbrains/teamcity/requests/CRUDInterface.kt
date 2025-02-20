package com.jetbrains.teamcity.requests

import com.jetbrains.teamcity.models.BaseModel

interface CRUDInterface {
    fun create(model: BaseModel): Any
    fun read(id: String): Any
    fun update(id: String, model: BaseModel): Any
    fun delete(id: String): Any
}
