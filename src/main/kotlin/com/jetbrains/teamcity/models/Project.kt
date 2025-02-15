package com.jetbrains.teamcity.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.jetbrains.teamcity.annotations.Random

@JsonIgnoreProperties(ignoreUnknown = true)
data class Project(
    @Random
    val id: String? = null,
    @Random
    val name: String? = null,
    val locator: String? = "_Root",
): BaseModel()
