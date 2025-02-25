package com.jetbrains.teamcity.ui

import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide.`$`
import com.jetbrains.teamcity.api.enums.Endpoint
import com.jetbrains.teamcity.api.models.BuildType
import com.jetbrains.teamcity.ui.constants.TestConstants
import com.jetbrains.teamcity.ui.pages.BuildTypePage
import com.jetbrains.teamcity.ui.pages.ProjectPage
import com.jetbrains.teamcity.ui.pages.admin.CreateBuildTypePage
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("Regression")
class CreateBuildTypeTest : BaseUiTest() {

    @Test
    @Tag("Positive")
    fun `a user should be able to create a build type`() {
        loginAs(testData.user)

        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)

        CreateBuildTypePage.open(testData.project.id!!)
            .createForm(TestConstants.repoUrl)
            .setupBuildType(testData.buildType.name!!)

        val createdBuildType = superUserCheckedRequests.getRequest<BuildType>(Endpoint.BUILD_TYPES)
            .search("name:${testData.buildType.name}")
        softy.assertThat(createdBuildType).isNotNull

        BuildTypePage.open(createdBuildType.project!!.id!!, createdBuildType.name!!).title.shouldHave(
            exactText(
                createdBuildType.name
            )
        )

        softy.assertThat(
            ProjectPage.open(createdBuildType.project.id)
                .getBuildTypes()
                .any { buildType -> buildType.name.text() == createdBuildType.name }
        ).isTrue
    }

    @Test
    @Tag("Negative")
    fun `a user should not be able to create a build type without a name`() {
        loginAs(testData.user)

        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)

        CreateBuildTypePage.open(testData.project.id!!)
            .createForm(TestConstants.repoUrl)
            .setupBuildType("")

        softy.assertThat(
            superUserCheckedRequests.getRequest<BuildType>(Endpoint.BUILD_TYPES)
                .filter(
                    filters = mapOf("project" to testData.project.id!!),
                    listJsonPath = Endpoint.BUILD_TYPES.listJsonPath
                )
        ).isEmpty()

        val error = `$`("#error_buildTypeName")
        softy.assertThat(error.`is`(visible))
        softy.assertThat(error.has(exactText("Build configuration name must not be empty")))
    }

    @Test
    @Tag("Negative")
    fun `a user should not be able to create a build type without a branch`() {
        loginAs(testData.user)

        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)

        val createBuildTypePage = CreateBuildTypePage.open(testData.project.id!!)
            .createForm(TestConstants.repoUrl)

        createBuildTypePage.branchInput.`val`("")
        createBuildTypePage.setupBuildType(testData.buildType.name!!)

        softy.assertThat(
            superUserCheckedRequests.getRequest<BuildType>(Endpoint.BUILD_TYPES)
                .filter(
                    filters = mapOf("project" to testData.project.id!!),
                    listJsonPath = Endpoint.BUILD_TYPES.listJsonPath
                )
        ).isEmpty()

        val error = `$`("#error_branch")
        softy.assertThat(error.`is`(visible))
        softy.assertThat(error.has(exactText("Branch name must be specified")))
    }
}
