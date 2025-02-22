package com.jetbrains.teamcity.api.enums

import com.jetbrains.teamcity.api.models.BaseModel
import com.jetbrains.teamcity.api.models.BuildRun
import com.jetbrains.teamcity.api.models.BuildType
import com.jetbrains.teamcity.api.models.Project
import com.jetbrains.teamcity.api.models.User

enum class Endpoint(
    val url: String,
    val modelClass: Class<out BaseModel>,
) {
    BUILD_TYPES("/app/rest/buildTypes", BuildType::class.java),
    BUILD_QUEUE("/app/rest/buildQueue", BuildRun::class.java),
    PROJECTS("/app/rest/projects", Project::class.java),
    USERS("/app/rest/users", User::class.java),
}
