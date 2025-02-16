package com.jetbrains.teamcity.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Role(
    val roleId: String = "SYSTEM_ADMIN",
    val scope: String = "g",
): BaseModel()
