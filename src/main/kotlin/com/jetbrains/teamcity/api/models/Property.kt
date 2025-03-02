package com.jetbrains.teamcity.api.models

data class Property(
    val name: String = "",
    val value: String = ""
) : BaseModel()
