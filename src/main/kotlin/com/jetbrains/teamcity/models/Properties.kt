package com.jetbrains.teamcity.models

data class Properties(
    val count: Int = 0,
    val property: List<Property> = emptyList(),
): BaseModel()
