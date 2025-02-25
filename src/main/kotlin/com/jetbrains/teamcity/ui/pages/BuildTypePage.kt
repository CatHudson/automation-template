package com.jetbrains.teamcity.ui.pages

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`

class BuildTypePage: BasePage() {
    val title = `$`("h1")

    companion object {

        private const val PROJECT_URL = "/buildConfiguration/%s_%s"

        fun open(projectId: String, buildTypeName: String): BuildTypePage {
            return Selenide.open(
                PROJECT_URL.format(projectId, buildTypeName.replace("_", "")),
                BuildTypePage::class.java
            )
        }
    }
}
