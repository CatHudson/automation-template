package com.jetbrains.teamcity.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.jetbrains.teamcity.annotations.Random

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
    @Random
    val username: String = "",
    @Random
    val password: String = "",
    val roles: Roles = Roles()
): BaseModel()
