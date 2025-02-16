package com.jetbrains.teamcity.api

import com.jetbrains.teamcity.enums.Endpoint
import com.jetbrains.teamcity.extensions.step
import com.jetbrains.teamcity.generators.TestDataGenerator
import com.jetbrains.teamcity.models.BuildType
import com.jetbrains.teamcity.models.Project
import com.jetbrains.teamcity.models.User
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
        val user = TestDataGenerator.generate(User::class.java)
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(user)

        val userCheckedRequester = CheckedRequests(Specification.authSpec(user))
        val project = TestDataGenerator.generate(Project::class.java)
        val buildType = TestDataGenerator.generate(listOf(project), BuildType::class.java)

        userCheckedRequester.getRequest(Endpoint.PROJECTS).create(project)
        userCheckedRequester.getRequest(Endpoint.BUILD_TYPES).create(buildType)

        val createdBuildType = userCheckedRequester.getRequest<BuildType>(Endpoint.BUILD_TYPES).read(buildType.id!!)

        softy.assertThat(createdBuildType.id).isNotEmpty
        softy.assertThat(createdBuildType.name).isEqualTo(buildType.name)
        softy.assertThat(createdBuildType.project?.id).isEqualTo(buildType.project?.id)
        softy.assertThat(createdBuildType.steps?.count).isEqualTo(buildType.steps?.count)
        softy.assertThat(createdBuildType.steps?.steps).isEqualTo(buildType.steps?.steps)
    }

    @Test
    @Tag("CRUD")
    @Tag("Negative")
    fun `user cannot create the second buildType with already existing id`() {
        val user = TestDataGenerator.generate(User::class.java)
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(user)

        val userCheckedRequester = CheckedRequests(Specification.authSpec(user))
        val project = TestDataGenerator.generate(Project::class.java)
        val buildType = TestDataGenerator.generate(listOf(project), BuildType::class.java)
        val buildType2 = TestDataGenerator.generate(listOf(project), BuildType::class.java, buildType.id!!)

        userCheckedRequester.getRequest(Endpoint.PROJECTS).create(project)
        userCheckedRequester.getRequest(Endpoint.BUILD_TYPES).create(buildType)

        UncheckedRequests(Specification.authSpec(user))
            .getRequest(Endpoint.BUILD_TYPES)
            .create(buildType2)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body(Matchers.containsString("The build configuration / template ID \"${buildType2.id}\" is already used by another configuration or template"))
    }

    @Test
    @Tag("Roles")
    @Tag("Positive")
    fun `project-admin creates a buildType`() {
        step("Create a user") {}
        step("Grant the user PROJECT_ADMIN privileges") {}
        step("Create a project by the project-admin") {}
        step("Create a build type for the project by the project-admin") {}
        step("Assert that the build type was created successfully") {}
    }

    @Test
    @Tag("Roles")
    @Tag("Negative")
    fun `project-admin cannot create a buildType for another project`() {
        step("Create a user") {}
        step("Grant the user PROJECT_ADMIN privileges") {}
        step("Create a project by the project-admin") {}

        step("Create the second user") {}
        step("Grant the second user PROJECT_ADMIN privileges") {}
        step("Create a project by the second project-admin") {}

        step("Create a build type for the second project by the first project-admin") {}
        step("Assert that the build type was not created with FORBIDDEN code") {}
    }
}
