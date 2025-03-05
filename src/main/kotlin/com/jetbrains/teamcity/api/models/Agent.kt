package com.jetbrains.teamcity.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Agent(
    val id: String,
    val name: String
): BaseModel()
