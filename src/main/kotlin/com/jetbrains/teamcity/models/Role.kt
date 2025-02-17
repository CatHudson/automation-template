package com.jetbrains.teamcity.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Role(
    val roleId: String = "SYSTEM_ADMIN",
    val scope: String = "g",
): BaseModel() {
    companion object {
        fun projectAdmin(scope: String): Role {
            return Role(
                roleId = "PROJECT_ADMIN",
                scope = "p:$scope",
            )
        }
    }
}
