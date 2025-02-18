package com.jetbrains.teamcity.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class BuildRun(
    val id: Long = 0,
    val buildType: BuildType = BuildType(),
    val status: String = "",
    val state: String = "",
): BaseModel()
