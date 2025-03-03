package com.jetbrains.teamcity.ui.pages

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.`$$`
import com.jetbrains.teamcity.ui.elements.ProjectElement
import io.qameta.allure.Step

class ProjectsPage : BasePage() {

    private val projectElements = `$$`("div[class*='Subproject__container']")
    private val header = `$`(".MainPanel__router--gF > div")
    private val searchProjectInput = `$`("#search-projects")

    init {
        header.shouldBe(visible, BASE_WAITING)
    }

    @Step("Search {projectName} project by name")
    fun searchProjectByName(projectName: String) {
        searchProjectInput.`val`(projectName).pressEnter().pressEnter()
    }

    @Step("Get a list of projects")
    fun getProjects(): List<ProjectElement> {
        return generatePageElements(projectElements) {
            ProjectElement(it)
        }
    }

    companion object {

        private const val PROJECTS_URL = "/favorite/projects"

        @Step("Open projects page")
        fun open(): ProjectsPage {
            return Selenide.open(PROJECTS_URL, ProjectsPage::class.java)
        }
    }
}
