package com.jetbrains.teamcity.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Role(
    val roleId: String = "SYSTEM_ADMIN",
    val scope: String = "g",
): BaseModel() {
    companion object {

        val projectDeveloper: Role by lazy {
            Role(
                roleId = "PROJECT_DEVELOPER",
                scope = "g"
            )
        }

        val projectViewer: Role by lazy {
            Role(
                roleId = "PROJECT_VIEWER",
                scope = "g",
            )
        }

        val agentManager: Role by lazy {
            Role(
                roleId = "AGENT_MANAGER",
                scope = "g",
            )
        }

        fun projectAdmin(scope: String): Role {
            return Role(
                roleId = "PROJECT_ADMIN",
                scope = "p:$scope",
            )
        }
    }
}
