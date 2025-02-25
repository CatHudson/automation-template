package com.jetbrains.teamcity.ui

import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide.`$`
import com.jetbrains.teamcity.api.enums.Endpoint
import com.jetbrains.teamcity.api.models.Project
import com.jetbrains.teamcity.ui.constants.TestConstants
import com.jetbrains.teamcity.ui.pages.ProjectPage
import com.jetbrains.teamcity.ui.pages.ProjectsPage
import com.jetbrains.teamcity.ui.pages.admin.CreateProjectPage
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("Regression")
class CreateProjectTest : BaseUiTest() {

    @Test
    @Tag("Positive")
    fun `a user should be able to create a project`() {
        loginAs(testData.user)

        CreateProjectPage
            .open(TestConstants.rootProjectLocator)
            .createForm(TestConstants.repoUrl)
            .setupProject(testData.project.name!!, testData.buildType.name!!)

        val createdProject =
            superUserCheckedRequests.getRequest<Project>(Endpoint.PROJECTS).search("name:${testData.project.name}")
        softy.assertThat(createdProject).isNotNull

        ProjectPage.open(createdProject.id!!).title.shouldHave(exactText(testData.project.name!!))

        softy.assertThat(
            ProjectsPage.open().getProjects().any { project -> project.name.text() == testData.project.name }).isTrue
    }

    @Test
    @Tag("Negative")
    fun `a user should not be able to create a project without a name`() {
        val initialProjectsAmount = superUserCheckedRequests
            .getRequest<Project>(Endpoint.PROJECTS)
            .filter(listJsonPath = Endpoint.PROJECTS.listJsonPath)
            .size

        loginAs(testData.user)

        CreateProjectPage
            .open(TestConstants.rootProjectLocator)
            .createForm(TestConstants.repoUrl)
            .setupProject(projectName = "", testData.buildType.name!!)

        val currentProjectsAmount = superUserCheckedRequests
            .getRequest<Project>(Endpoint.PROJECTS)
            .filter(listJsonPath = Endpoint.PROJECTS.listJsonPath)
            .size

        softy.assertThat(currentProjectsAmount).isEqualTo(initialProjectsAmount)

        val error = `$`("#error_projectName")
        softy.assertThat(error.`is`(visible))
        softy.assertThat(error.has(exactText("Project name must not be empty")))
    }
}
