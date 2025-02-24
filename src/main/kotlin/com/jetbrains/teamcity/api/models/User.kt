package com.jetbrains.teamcity.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.jetbrains.teamcity.api.annotations.Random

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
    val id: Long? = null,
    @Random
    val username: String? = null,
    @Random
    val password: String = "",
    val roles: Roles = Roles()
): BaseModel()
