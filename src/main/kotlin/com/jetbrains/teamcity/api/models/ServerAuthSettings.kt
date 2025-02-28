package com.jetbrains.teamcity.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ServerAuthSettings(
    val perProjectPermissions: Boolean = false,
    val modules: AuthModules = AuthModules(),
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthModules(
    val module: List<AuthModule> = emptyList(),
) : BaseModel()

@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthModule(
    val name: String = "HTTP-Basic",
) : BaseModel()
