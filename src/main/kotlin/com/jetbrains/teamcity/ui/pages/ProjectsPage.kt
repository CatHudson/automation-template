package com.jetbrains.teamcity.ui.pages

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.`$$`
import com.jetbrains.teamcity.ui.elements.ProjectElement

class ProjectsPage : BasePage() {

    private val projectElements = `$$`("div[class*='Subproject__container']")
    private val header = `$`(".MainPanel__router--gF > div")
    private val searchProjectInput = `$`("#search-projects")

    init {
        header.shouldBe(visible, BASE_WAITING)
    }

    fun searchProjectByName(projectName: String) {
        searchProjectInput.`val`(projectName).pressEnter().pressEnter()
    }

    fun getProjects(): List<ProjectElement> {
        return generatePageElements(projectElements) {
            ProjectElement(it)
        }
    }

    companion object {

        private const val PROJECTS_URL = "/favorite/projects"

        fun open(): ProjectsPage {
            return Selenide.open(PROJECTS_URL, ProjectsPage::class.java)
        }
    }
}
