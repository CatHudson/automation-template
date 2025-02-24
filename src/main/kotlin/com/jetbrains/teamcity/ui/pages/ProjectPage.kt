package com.jetbrains.teamcity.ui.pages

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`

class ProjectPage : BasePage() {

    companion object {

        private const val PROJECT_URL = "/project/%s"

        fun open(projectId: String): ProjectPage {
            return Selenide.open(
                PROJECT_URL.format(projectId),
                ProjectPage::class.java
            )
        }
    }

    val title = `$`("span[class*='ProjectPageHeader__title']")
}
