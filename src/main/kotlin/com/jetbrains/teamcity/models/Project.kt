package com.jetbrains.teamcity.models

data class Project(
    private val id: Int,
    private val name: String,
    private val locator: String = "_Root",
): BaseModel()
