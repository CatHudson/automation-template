package com.jetbrains.teamcity.ui

import com.codeborne.selenide.Condition
import com.jetbrains.teamcity.api.enums.Endpoint
import com.jetbrains.teamcity.api.models.Step
import com.jetbrains.teamcity.api.models.Steps
import com.jetbrains.teamcity.ui.pages.BuildTypePage
import com.jetbrains.teamcity.ui.pages.ProjectPage
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("Regression")
class StartBuildTest : BaseUiTest() {

    @Test
    fun `a user should be able to start a successful build`() {
        loginAs(testData.user)

        val buildTypeWithStep = testData.buildType.copy(steps = Steps(1, listOf(Step.printHelloWorldStep)))

        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(buildTypeWithStep.project!!)
        superUserCheckedRequests.getRequest(Endpoint.BUILD_TYPES).create(buildTypeWithStep)

        ProjectPage.open(testData.project.id!!)
            .getBuildTypes()
            .first { buildType ->
                buildType.name.text() == testData.buildType.name
            }
            .runBuildButton.click()

        val buildTypePage = BuildTypePage.open(buildTypeWithStep.id!!)
        val buildRuns = buildTypePage.getBuildRuns()

        if (buildRuns.isNotEmpty()) {
            softy.assertThat(buildRuns).hasSize(1)
            buildRuns.first().buildStatus.shouldHave(Condition.exactText("Success"))
        } else {
            buildTypePage.buildsInQueueIndicator.shouldHave(Condition.text("1 build in queue"))
        }
    }
}
