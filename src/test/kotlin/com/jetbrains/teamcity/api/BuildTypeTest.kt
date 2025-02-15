package com.jetbrains.teamcity.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.jetbrains.teamcity.enums.Endpoint
import com.jetbrains.teamcity.extensions.step
import com.jetbrains.teamcity.generators.TestDataGenerator
import com.jetbrains.teamcity.models.BuildType
import com.jetbrains.teamcity.models.Project
import com.jetbrains.teamcity.models.Step
import com.jetbrains.teamcity.models.Steps
import com.jetbrains.teamcity.models.User
import com.jetbrains.teamcity.requests.checked.CheckedRequestBase
import com.jetbrains.teamcity.spec.Specification
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("Regression")
class BuildTypeTest : BaseApiTest() {

    @Test
    @Tag("CRUD")
    @Tag("Positive")
    fun `user creates a buildType`() {
        val user = TestDataGenerator.generate(User::class.java)
        val project = TestDataGenerator.generate(Project::class.java)
        val buildType = TestDataGenerator.generate(BuildType::class.java)
            .copy(
                project = Project(
                    id = project.id,
                    locator = null
                )
            )
        lateinit var createdBuildType: BuildType
        step("Create a user") {
            CheckedRequestBase<User>(Specification.superUserSpec(), Endpoint.USERS)
                .create(user)
        }
        step("Create a project by the user") {
            CheckedRequestBase<Project>(Specification.superUserSpec(), Endpoint.PROJECTS)
                .create(project)
        }
        step("Create a build type for the project by the user") {
            createdBuildType = CheckedRequestBase<BuildType>(Specification.superUserSpec(), Endpoint.BUILD_TYPES)
                .create(buildType)
        }
        step("Assert that the build type was created successfully with correct data") {
            softy.assertThat(createdBuildType.id).isNotEmpty
            softy.assertThat(createdBuildType.name).isEqualTo(buildType.name)
            softy.assertThat(createdBuildType.project?.id).isEqualTo(buildType.project?.id)
            softy.assertThat(createdBuildType.steps?.count).isEqualTo(buildType.steps?.count)
            softy.assertThat(createdBuildType.steps?.steps).isEqualTo(buildType.steps?.steps)
        }
    }

    @Test
    @Tag("CRUD")
    @Tag("Negative")
    fun `user cannot create the second buildType with already existing id`() {
        step("Create a user") {}
        step("Create a project by the user") {}
        step("Create a build type for the project by the user") {}
        step("Create the second build type for the project by the user") {}
        step("Assert that the second build type was not created due to already existing id") {}
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
