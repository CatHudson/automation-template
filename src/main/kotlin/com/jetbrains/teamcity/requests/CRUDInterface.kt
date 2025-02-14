package com.jetbrains.teamcity.requests

import com.jetbrains.teamcity.models.BaseModel

interface CRUDInterface {
    fun create(model: BaseModel): Any
    fun read(id: Int): Any
    fun update(id: Int, model: BaseModel): Any
    fun delete(id: Int): Any
}
