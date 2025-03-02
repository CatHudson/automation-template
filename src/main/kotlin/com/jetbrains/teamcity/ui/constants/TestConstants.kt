package com.jetbrains.teamcity.ui.constants

import com.jetbrains.teamcity.api.configuration.Configuration

object TestConstants {
    val repoUrl = Configuration.getProperty("repo-url").toString()
    val rootProjectLocator = Configuration.getProperty("root-project-locator").toString()
}
