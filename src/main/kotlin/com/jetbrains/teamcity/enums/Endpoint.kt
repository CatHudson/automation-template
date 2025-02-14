package com.jetbrains.teamcity.enums

import com.jetbrains.teamcity.models.BaseModel
import com.jetbrains.teamcity.models.BuildType
import com.jetbrains.teamcity.models.Project
import kotlin.reflect.KClass

enum class Endpoint(
    val url: String,
    val modelClass: Class<out BaseModel>,
) {
    BUILD_TYPES("buildTypes", BuildType::class.java,),
    PROJECT("/app/rest/projects", Project::class.java,),
}
