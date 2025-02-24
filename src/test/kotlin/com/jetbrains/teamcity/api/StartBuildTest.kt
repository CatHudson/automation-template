package com.jetbrains.teamcity.api

import com.github.tomakehurst.wiremock.client.WireMock.post
import com.jetbrains.teamcity.api.common.WireMock
import com.jetbrains.teamcity.api.enums.Endpoint
import com.jetbrains.teamcity.api.generators.TestDataGenerator
import com.jetbrains.teamcity.api.models.BuildRun
import com.jetbrains.teamcity.api.models.Step
import com.jetbrains.teamcity.api.models.Steps
import com.jetbrains.teamcity.api.requests.checked.CheckedRequests
import com.jetbrains.teamcity.api.spec.Specification
import io.qameta.allure.Feature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.apache.http.HttpStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Feature("Start build")
class StartBuildTest: BaseApiTest() {

    @BeforeEach
    fun `setup Wiremock server`() {
        val fakeBuild = BuildRun(
            state = "finished",
            status = "SUCCESS"
        )
        WireMock.setupServer(
            mappingBuilder = post(Endpoint.BUILD_QUEUE.url),
            status = HttpStatus.SC_OK,
            model = fakeBuild
        )
    }

    @AfterEach
    fun stopWiremock() {
        WireMock.stopServer()
    }

    //Advanced homework ↓↓↓

    @Test
    fun `a user creates a successful build, mocked test`() {
        val buildResult = CheckedRequests(Specification.mockSpec())
            .getRequest(Endpoint.BUILD_QUEUE)
            .create(TestDataGenerator.generate(BuildRun::class.java)) as BuildRun

        softy.assertThat(buildResult.status).isEqualTo(BuildRun.Status.SUCCESS.value)
        softy.assertThat(buildResult.state).isEqualTo(BuildRun.State.FINISHED.value)
    }

    @Test
    @Tag("CRUD")
    @Tag("Positive")
    fun `create and launch a buildType with 'hello, world' in console`() = runTest {
        val buildTypeWithStep = testData.buildType.copy(steps = Steps(1, listOf(Step.printHelloWorldStep)))
        val buildRun = TestDataGenerator.generate(listOf(buildTypeWithStep), BuildRun::class.java)

        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)
        superUserCheckedRequests.getRequest(Endpoint.BUILD_TYPES).create(buildTypeWithStep)
        val createdBuildRun = superUserCheckedRequests.getRequest(Endpoint.BUILD_QUEUE).create(buildRun) as BuildRun

        val buildResult = waitForBuildCompletion(createdBuildRun)

        softy.assertThat(buildResult.buildType.id).isEqualTo(buildTypeWithStep.id)
        softy.assertThat(buildResult.buildType.name).isEqualTo(buildTypeWithStep.name)
        softy.assertThat(buildResult.status).isEqualTo("SUCCESS")
    }

    private suspend fun waitForBuildCompletion(createdBuildRun: BuildRun): BuildRun {
        var result: BuildRun? = null
        var attempts = 0
        val maxAttempts = 20

        while (result?.state != "finished" && attempts < maxAttempts) {
            result = superUserCheckedRequests
                .getRequest(Endpoint.BUILD_QUEUE)
                .read(createdBuildRun.id.toString()) as BuildRun

            attempts++
            withContext(Dispatchers.Default) {
                delay(500)
            }
        }

        if (attempts >= maxAttempts)
            throw AssertionError("The build configuration has exceeded $maxAttempts attempts")

        return result!!
    }
}
