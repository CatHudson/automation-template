package com.jetbrains.teamcity.models

import com.jetbrains.teamcity.annotations.Optional
import com.jetbrains.teamcity.annotations.Parameterizable
import com.jetbrains.teamcity.annotations.Random

data class BuildType(
    private val id: Int,
    @Random
    private val name: String,
    @Parameterizable
    private val project: Project,
    @Optional
    private val steps: Steps,
): BaseModel()
