package com.jetbrains.teamcity.models

data class Property(
    val name: String = "",
    val value: String = "",
): BaseModel()
