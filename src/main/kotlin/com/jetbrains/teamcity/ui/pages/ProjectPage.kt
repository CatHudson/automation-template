package com.jetbrains.teamcity.ui.pages

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.`$$`
import com.jetbrains.teamcity.ui.elements.BuildTypeElement

class ProjectPage : BasePage() {

    val title = `$`("span[class*='ProjectPageHeader__title']")
    private val buildTypes = `$$`("div[class*='buildType']")

    init {
        title.shouldBe(visible, BASE_WAITING)
    }

    fun getBuildTypes(): List<BuildTypeElement> {
        return generatePageElements(buildTypes) {
            BuildTypeElement(it)
        }
    }

    companion object {

        private const val PROJECT_URL = "/project/%s"

        fun open(projectId: String): ProjectPage {
            return Selenide.open(
                PROJECT_URL.format(projectId),
                ProjectPage::class.java
            )
        }
    }
}
