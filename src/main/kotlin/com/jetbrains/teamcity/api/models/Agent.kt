package com.jetbrains.teamcity.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Agent(
    val id: String? = null,
    val name: String? = null
) : BaseModel()
