package com.jetbrains.teamcity.api

import com.jetbrains.teamcity.enums.Endpoint
import com.jetbrains.teamcity.extensions.step
import com.jetbrains.teamcity.generators.TestDataGenerator
import com.jetbrains.teamcity.models.User
import com.jetbrains.teamcity.requests.checked.CheckedRequestBase
import com.jetbrains.teamcity.spec.Specification
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("Regression")
class BuildTypeTest: BaseApiTest() {

    @Test
    @Tag("CRUD")
    @Tag("Positive")
    fun `user creates a buildType`() {
        step("Create a user") {
            val user = TestDataGenerator.generate(User::class.java)
            val requester = CheckedRequestBase<User>(Specification.superUserAuth(), Endpoint.USER)
            requester.create(user)
        }
        step("Create a project by the user") {

        }
        step("Create a build type for the project by the user") {

        }
        step("Assert that the build type was created successfully with correct data") {

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
