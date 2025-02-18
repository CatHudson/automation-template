package com.jetbrains.teamcity.models

data class PesData(
    val user: User = User(),
    val project: Project = Project(),
    val buildType: BuildType = BuildType(),
)
