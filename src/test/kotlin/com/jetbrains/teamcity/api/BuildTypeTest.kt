package com.jetbrains.teamcity.api

import com.jetbrains.teamcity.api.enums.Endpoint
import com.jetbrains.teamcity.api.generators.TestDataGenerator
import com.jetbrains.teamcity.api.models.BuildType
import com.jetbrains.teamcity.api.models.Project
import com.jetbrains.teamcity.api.models.Role
import com.jetbrains.teamcity.api.models.Roles
import com.jetbrains.teamcity.api.requests.checked.CheckedRequests
import com.jetbrains.teamcity.api.requests.unchecked.UncheckedRequests
import com.jetbrains.teamcity.api.spec.ResponseValidationSpecifications
import com.jetbrains.teamcity.api.spec.Specification
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

        val createdBuildType = userCheckedRequester.getRequest<BuildType>(
            Endpoint.BUILD_TYPES
        ).read(testData.buildType.id!!)

        softy.assertThat(createdBuildType).isEqualTo(testData.buildType)
    }

    @Test
    @Tag("CRUD")
    @Tag("Negative")
    fun `user cannot create the second buildType with already existing id`() {
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(testData.user)

        val userCheckedRequester = CheckedRequests(Specification.authSpec(testData.user))
        val duplicatedBuildType = TestDataGenerator.generate(
            listOf(testData.project),
            BuildType::class.java,
            testData.buildType.id!!
        )

        userCheckedRequester.getRequest(Endpoint.PROJECTS).create(testData.project)
        userCheckedRequester.getRequest(Endpoint.BUILD_TYPES).create(testData.buildType)

        UncheckedRequests(Specification.authSpec(testData.user))
            .getRequest(Endpoint.BUILD_TYPES)
            .create(duplicatedBuildType)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body(
                Matchers.containsString(
                    "The build configuration / template ID \"${duplicatedBuildType.id}\" is already used by another configuration or template"
                )
            )
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

        val createdBuildType = projectAdminCheckedRequester.getRequest<BuildType>(
            Endpoint.BUILD_TYPES
        ).read(testData.buildType.id!!)

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
            .spec(ResponseValidationSpecifications.checkForbiddenError())
    }
}
