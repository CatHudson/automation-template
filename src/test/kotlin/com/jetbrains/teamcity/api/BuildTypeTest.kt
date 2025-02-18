package com.jetbrains.teamcity.api

import com.jetbrains.teamcity.enums.Endpoint
import com.jetbrains.teamcity.generators.TestDataGenerator
import com.jetbrains.teamcity.models.BuildRun
import com.jetbrains.teamcity.models.BuildType
import com.jetbrains.teamcity.models.Project
import com.jetbrains.teamcity.models.Role
import com.jetbrains.teamcity.models.Roles
import com.jetbrains.teamcity.models.Step
import com.jetbrains.teamcity.models.Steps
import com.jetbrains.teamcity.requests.checked.CheckedRequests
import com.jetbrains.teamcity.requests.unchecked.UncheckedRequests
import com.jetbrains.teamcity.spec.Specification
import org.apache.http.HttpStatus
import org.hamcrest.Matchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("Regression")
class BuildTypeTest : BaseApiTest() {

    @Test
    @Tag("CRUD")
    @Tag("Positive")
    fun `user creates a buildType`() {
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(testData.user)

        val userCheckedRequester = CheckedRequests(Specification.authSpec(testData.user))

        userCheckedRequester.getRequest(Endpoint.PROJECTS).create(testData.project)
        userCheckedRequester.getRequest(Endpoint.BUILD_TYPES).create(testData.buildType)

        val createdBuildType = userCheckedRequester.getRequest<BuildType>(Endpoint.BUILD_TYPES).read(testData.buildType.id!!)

        softy.assertThat(createdBuildType.id).isNotEmpty
        softy.assertThat(createdBuildType.name).isEqualTo(testData.buildType.name)
        softy.assertThat(createdBuildType.project?.id).isEqualTo(testData.buildType.project?.id)
        softy.assertThat(createdBuildType.project?.name).isEqualTo(testData.buildType.project?.name)
        softy.assertThat(createdBuildType.steps?.count).isEqualTo(testData.buildType.steps?.count)
        softy.assertThat(createdBuildType.steps?.step).isEqualTo(testData.buildType.steps?.step)
    }

    @Test
    @Tag("CRUD")
    @Tag("Negative")
    fun `user cannot create the second buildType with already existing id`() {
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(testData.user)

        val userCheckedRequester = CheckedRequests(Specification.authSpec(testData.user))
        val duplicatedBuildType = TestDataGenerator.generate(listOf(testData.project), BuildType::class.java, testData.buildType.id!!)

        userCheckedRequester.getRequest(Endpoint.PROJECTS).create(testData.project)
        userCheckedRequester.getRequest(Endpoint.BUILD_TYPES).create(testData.buildType)

        UncheckedRequests(Specification.authSpec(testData.user))
            .getRequest(Endpoint.BUILD_TYPES)
            .create(duplicatedBuildType)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body(Matchers.containsString("The build configuration / template ID \"${duplicatedBuildType.id}\" is already used by another configuration or template"))
    }

    @Test
    @Tag("Roles")
    @Tag("Positive")
    fun `project-admin creates a buildType`() {
        val projectAdminUser = testData.user.copy(roles = Roles(listOf(Role.projectAdmin(testData.project.id!!))))
        val projectAdminCheckedRequester = CheckedRequests(Specification.authSpec(projectAdminUser))

        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(projectAdminUser)
        projectAdminCheckedRequester.getRequest(Endpoint.BUILD_TYPES).create(testData.buildType)

        val createdBuildType = projectAdminCheckedRequester.getRequest<BuildType>(Endpoint.BUILD_TYPES).read(testData.buildType.id!!)

        softy.assertThat(createdBuildType.name).isEqualTo(testData.buildType.name)
    }

    @Test
    @Tag("Roles")
    @Tag("Negative")
    fun `project-admin cannot create a buildType for another project`() {
        val projectAdminUser = testData.user.copy(roles = Roles(listOf(Role.projectAdmin(testData.project.id!!))))
        val anotherProject = TestDataGenerator.generate(Project::class.java)
        val buildTypeForAnotherProject = TestDataGenerator.generate(listOf(anotherProject), BuildType::class.java)

        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)
        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(anotherProject)
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(projectAdminUser)

        UncheckedRequests(Specification.authSpec(projectAdminUser))
            .getRequest(Endpoint.BUILD_TYPES)
            .create(buildTypeForAnotherProject)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_FORBIDDEN)
            .body(Matchers.containsString("Access denied. Check the user has enough permissions to perform the operation."))
    }

    //Advanced homework ↓↓↓

    @Test
    @Tag("CRUD")
    @Tag("Positive")
    fun `create and launch a buildType with 'hello, world' in console`() {
        val buildTypeWithStep = testData.buildType.copy(steps = Steps(1, listOf(Step.printHelloWorldStep)))
        val buildRun = TestDataGenerator.generate(listOf(buildTypeWithStep), BuildRun::class.java)

        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)
        superUserCheckedRequests.getRequest(Endpoint.BUILD_TYPES).create(buildTypeWithStep)
        val createdBuildRun = superUserCheckedRequests.getRequest(Endpoint.BUILD_QUEUE).create(buildRun) as BuildRun

        //we can make the test a little longer to assert the build run was successful
        val buildResult = waitForBuildCompletion(createdBuildRun)

        softy.assertThat(buildResult.buildType.id).isEqualTo(buildTypeWithStep.id)
        softy.assertThat(buildResult.buildType.name).isEqualTo(buildTypeWithStep.name)
        softy.assertThat(buildResult.status).isEqualTo("SUCCESS")
    }

    private fun waitForBuildCompletion(createdBuildRun: BuildRun): BuildRun {
        var result: BuildRun? = null
        var attempts = 0
        val maxAttempts = 20

        while (result?.state != "finished" && attempts < maxAttempts) {
            result = superUserCheckedRequests
                .getRequest(Endpoint.BUILD_QUEUE)
                .read(createdBuildRun.id.toString()) as BuildRun

            attempts++
            Thread.sleep(500)
        }

        if (attempts >= maxAttempts)
            throw AssertionError("The build configuration has been exceeded $maxAttempts attempts")

        return result!!
    }
}
