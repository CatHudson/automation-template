package com.jetbrains.teamcity.ui

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideConfig
import com.jetbrains.teamcity.api.enums.Endpoint
import com.jetbrains.teamcity.api.extensions.step
import com.jetbrains.teamcity.api.models.Project
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import ui.pages.ProjectPage
import ui.pages.admin.CreateProjectPage

@Tag("Regression")
class CreateProjectTest : BaseUiTest() {

    companion object {
        private const val REPO_URL = "https://github.com/CatHudson/wtf-homework.git"
        private const val ROOT_PROJECT_LOCATOR = "_Root"
    }

    @Test
    @Tag("Positive")
    fun `a user should be able to create a project`() {
        loginAs(testData.user)
        
        CreateProjectPage
            .open(ROOT_PROJECT_LOCATOR)
            .createForm(REPO_URL)
            .setupProject(testData.project.name!!, testData.buildType.name!!)

        val createdProject = superUserCheckedRequests.getRequest<Project>(Endpoint.PROJECTS).search("name:${testData.project.name}")
        softy.assertThat(createdProject).isNotNull

        ProjectPage.open(createdProject.id!!).title.shouldHave(Condition.exactText(testData.project.name!!))
    }

    @Test
    @Tag("Negative")
    fun `a user should not be able to create a project without a name`() {
        //setup
        step("Login as user") { }
        step("Fix the amount of existing projects") { }

        //action on the UI
        step("Open `create project page` http://host.docker.internal:8111/admin/createObjectMenu.html") { }
        step("Send all project parameters (repo URL)") {}
        step("Click `proceed`") {}
        step("Set project name as an empty string") {}
        step("Click `proceed`") {}

        //check on the API level
        step("Check that the amount of existing projects did not change") { }

        //check the processed data displayed on the UI
        step("Check error on the UI level") {}
    }
}