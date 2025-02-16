package com.jetbrains.teamcity.models

data class Steps(
    val count: Int = 0,
    val steps: List<Step> = emptyList(),
): BaseModel()
