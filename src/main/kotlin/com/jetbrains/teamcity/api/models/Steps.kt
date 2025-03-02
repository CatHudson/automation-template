package com.jetbrains.teamcity.api.models

data class Steps(
    val count: Int = 0,
    val step: List<Step> = emptyList()
) : BaseModel()
