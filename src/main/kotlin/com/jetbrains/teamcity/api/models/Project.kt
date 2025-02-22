package com.jetbrains.teamcity.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.jetbrains.teamcity.api.annotations.Random

@JsonIgnoreProperties(ignoreUnknown = true)
data class Project(
    @Random
    val id: String? = null,
    @Random
    val name: String? = null,
    val locator: String? = null,
    val copyAllAssociatedSettings: Boolean? = null,
    val sourceProject: Locator? = null,
): BaseModel()
