package com.jetbrains.teamcity.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Locator(
    val locator: String? = null,
)
