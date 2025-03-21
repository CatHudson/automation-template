package com.jetbrains.teamcity.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.jetbrains.teamcity.api.annotations.Optional
import com.jetbrains.teamcity.api.annotations.Parameterizable
import com.jetbrains.teamcity.api.annotations.Random

@JsonIgnoreProperties(ignoreUnknown = true)
data class BuildType(
    @Random
    @Parameterizable
    val id: String? = null,
    @Random
    val name: String? = null,
    @Parameterizable
    val project: Project? = null,
    @Optional
    val steps: Steps? = Steps()
) : BaseModel()
