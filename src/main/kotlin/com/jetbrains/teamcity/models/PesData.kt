package com.jetbrains.teamcity.models

data class PesData(
    val project: Project = Project(),
    val user: User = User(),
    val buildType: BuildType = BuildType(),
)
