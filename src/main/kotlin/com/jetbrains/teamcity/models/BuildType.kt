package com.jetbrains.teamcity.models

data class BuildType(
    private val id: Int,
    private val name: String,
    private val project: Project,
    private val steps: Steps,
): BaseModel()
