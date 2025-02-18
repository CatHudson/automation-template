package com.jetbrains.teamcity.requests

import com.jetbrains.teamcity.enums.ReadQueryIdType
import com.jetbrains.teamcity.models.BaseModel

interface CRUDInterface {
    fun create(model: BaseModel): Any
    fun read(value: String, param: ReadQueryIdType = ReadQueryIdType.ID): Any
    fun update(id: String, model: BaseModel): Any
    fun delete(id: String): Any
}
