package com.jetbrains.teamcity.api.models

data class Properties(
    val count: Int = 0,
    val property: List<Property> = emptyList(),
): BaseModel()
