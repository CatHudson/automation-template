package com.jetbrains.teamcity.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class BuildRun(
    val id: Long = 0,
    val buildType: BuildType = BuildType(),
    val status: String = "",
    val state: String = "",
): BaseModel() {

    enum class Status(val value: String) {
        SUCCESS("SUCCESS")
    }

    enum class State(val value: String) {
        FINISHED("finished")
    }
}
