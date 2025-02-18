package com.jetbrains.teamcity.api

import com.jetbrains.teamcity.enums.Endpoint
import com.jetbrains.teamcity.enums.ReadQueryIdType
import com.jetbrains.teamcity.generators.RandomData
import com.jetbrains.teamcity.generators.TestDataGenerator
import com.jetbrains.teamcity.models.Project
import com.jetbrains.teamcity.models.Role
import com.jetbrains.teamcity.models.Roles
import com.jetbrains.teamcity.models.User
import com.jetbrains.teamcity.requests.unchecked.UncheckedRequests
import com.jetbrains.teamcity.spec.Specification
import org.apache.http.HttpStatus
import org.hamcrest.Matchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource

@Tag("Regression")
@Tag("Project")
class ProjectTest : BaseApiTest() {

    @Test
    @Tag("Positive")
    fun `a project can be created if valid data is provided`() {
        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)
        val createdProject = superUserCheckedRequests.getRequest<Project>(Endpoint.PROJECTS).read(testData.project.id!!)

        softy.assertThat(createdProject.id).isEqualTo(testData.project.id)
        softy.assertThat(createdProject.name).isEqualTo(testData.project.name)
    }

    @ParameterizedTest
    @Tag("Negative")
    @NullSource
    @ValueSource(strings = ["", " "])
    fun `a project cannot be created with an empty or null name`(name: String?) {
        superUserUncheckedRequests
            .getRequest(Endpoint.PROJECTS)
            .create(testData.project.copy(name = name))
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST) //throws 500: INTERNAL for a whitespace string
            .body(Matchers.containsString("Project name cannot be empty"))
    }

    @ParameterizedTest
    @Tag("Negative")
    @ValueSource(strings = ["", " ", "1", " test"])
    fun `a project cannot be created with invalid id`(id: String) {
        superUserUncheckedRequests
            .getRequest(Endpoint.PROJECTS)
            .create(testData.project.copy(id = id))
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST) //actually throws 500: INTERNAL
    }

    @Test
    @Tag("Negative")
    fun `a project cannot be created without auth`() {
        UncheckedRequests(Specification.unAuthSpec())
            .getRequest(Endpoint.PROJECTS)
            .create(testData.project)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
            .body(Matchers.containsString("Authentication required"))
    }

    @ParameterizedTest
    @Tag("Negative")
    @MethodSource("usersWithoutProjectCreationRights")
    fun `a project cannot be created by a user without admin rights`(user: User) {
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(user)

        UncheckedRequests(Specification.authSpec(user))
            .getRequest(Endpoint.PROJECTS)
            .create(testData.project)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_FORBIDDEN)
            .body(Matchers.containsString("Access denied"))
    }

    //Advanced homework ↓↓↓

    @Test
    @Tag("Positive")
    fun `a project may be found by name`() {
        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)

        val createdProject = superUserCheckedRequests
            .getRequest<Project>(Endpoint.PROJECTS)
            .read(
                value = testData.project.name!!,
                param = ReadQueryIdType.NAME
            )

        softy.assertThat(createdProject.id).isEqualTo(testData.project.id)
        softy.assertThat(createdProject.name).isEqualTo(testData.project.name)
    }

    @Test
    @Tag("Positive")
    fun `a non-existent project request returns 404 not found`() {
        superUserUncheckedRequests
            .getRequest(Endpoint.PROJECTS)
            .read(RandomData.string)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @Test
    @Tag("Positive")
    fun `a project cannot be found by name by an unauthorized user`() {
        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)

        UncheckedRequests(Specification.unAuthSpec())
            .getRequest(Endpoint.PROJECTS)
            .read(
                value = testData.project.name!!,
                param = ReadQueryIdType.NAME
            )
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
            .body(Matchers.containsString("Authentication required"))
    }

    companion object {
        @JvmStatic
        private fun usersWithoutProjectCreationRights(): List<User> {
            val projectViewer = TestDataGenerator.generate().user.copy(roles = Roles(listOf(Role.projectViewer)))
            val projectDeveloper = TestDataGenerator.generate().user.copy(roles = Roles(listOf(Role.projectDeveloper)))
            val agentManager = TestDataGenerator.generate().user.copy(roles = Roles(listOf(Role.agentManager)))
            return listOf(projectViewer, projectDeveloper, agentManager)
        }
    }
}
