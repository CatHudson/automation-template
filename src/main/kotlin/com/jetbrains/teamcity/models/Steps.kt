package com.jetbrains.teamcity.models

data class Steps(
    private val count: Int,
    private val steps: List<Step>,
): BaseModel()
