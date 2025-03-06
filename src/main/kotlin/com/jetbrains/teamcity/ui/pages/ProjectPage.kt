package com.jetbrains.teamcity.ui.pages

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.`$$`
import com.jetbrains.teamcity.ui.elements.BuildTypeElement
import io.qameta.allure.Step

class ProjectPage : BasePage() {

    val title = `$`("span[class*='ProjectPageHeader__title']")
    private val buildTypes = `$$`("div[class*='BuildTypes__list'] div[class*='BuildTypes__item']")

    init {
        title.shouldBe(visible, BASE_WAITING)
    }

    @Step("Get a list of build types")
    fun getBuildTypes(): List<BuildTypeElement> {
        return generatePageElements(buildTypes) {
            BuildTypeElement(it)
        }
    }

    companion object {

        private const val PROJECT_URL = "/project/%s"

        @Step("Open project {projectId} page")
        fun open(projectId: String): ProjectPage {
            return Selenide.open(
                PROJECT_URL.format(projectId),
                ProjectPage::class.java
            )
        }
    }
}
