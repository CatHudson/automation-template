package com.jetbrains.teamcity.api

import com.jetbrains.teamcity.api.enums.Endpoint
import com.jetbrains.teamcity.api.generators.RandomData
import com.jetbrains.teamcity.api.generators.TestDataGenerator
import com.jetbrains.teamcity.api.models.Locator
import com.jetbrains.teamcity.api.models.Project
import com.jetbrains.teamcity.api.models.Role
import com.jetbrains.teamcity.api.models.Roles
import com.jetbrains.teamcity.api.models.User
import com.jetbrains.teamcity.api.requests.unchecked.UncheckedRequests
import com.jetbrains.teamcity.api.spec.ResponseValidationSpecifications
import com.jetbrains.teamcity.api.spec.Specification
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
    fun `a user can create a project with valid data`() {
        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)
        val createdProject = superUserCheckedRequests.getRequest<Project>(Endpoint.PROJECTS).read(testData.project.id!!)

        softy.assertThat(createdProject).isEqualTo(testData.project)
    }

    @Test
    @Tag("Positive")
    fun `a user can create a project with one symbol length id`() {
        val project = testData.project.copy(id = RandomData.getString(1))
        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(project)
        val createdProject = superUserCheckedRequests.getRequest<Project>(Endpoint.PROJECTS).read(project.id!!)

        softy.assertThat(createdProject).isEqualTo(project)
    }

    @Test
    @Tag("Positive")
    fun `a user can create a project with 225 symbols length id`() {
        val project = testData.project.copy(id = RandomData.getString(225))
        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(project)
        val createdProject = superUserCheckedRequests.getRequest<Project>(Endpoint.PROJECTS).read(project.id!!)

        softy.assertThat(createdProject).isEqualTo(project)
    }

    @Test
    @Tag("Positive")
    fun `a user can create a project with cyrillic name`() {
        val project = testData.project.copy(name = RandomData.getCyrillicString())
        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(project)
        val createdProject = superUserCheckedRequests.getRequest<Project>(Endpoint.PROJECTS).read(project.id!!)

        softy.assertThat(createdProject).isEqualTo(project)
    }

    @Test
    @Tag("Positive")
    fun `a user should be able to copy a project`() {
        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)
        val originalProject = superUserCheckedRequests.getRequest<Project>(Endpoint.PROJECTS).read(testData.project.id!!)

        val copiedProject = TestDataGenerator.generate().project.copy(sourceProject = Locator(originalProject.id!!))
        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(copiedProject)

        val createdCopiedProject = superUserCheckedRequests.getRequest<Project>(Endpoint.PROJECTS).read(copiedProject.id!!)

        softy.assertThat(createdCopiedProject.id).isEqualTo(copiedProject.id)
        softy.assertThat(createdCopiedProject.name).isEqualTo(copiedProject.name)
    }

    @Test
    @Tag("Positive")
    fun `a user should be able to copy a project with 'copyAllAssociatedSettings' false`() {
        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)
        val originalProject = superUserCheckedRequests.getRequest<Project>(Endpoint.PROJECTS).read(testData.project.id!!)

        val copiedProject = TestDataGenerator.generate().project.copy(copyAllAssociatedSettings = false, sourceProject = Locator(originalProject.id!!))
        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(copiedProject)

        val createdCopiedProject = superUserCheckedRequests.getRequest<Project>(Endpoint.PROJECTS).read(copiedProject.id!!)

        softy.assertThat(createdCopiedProject.id).isEqualTo(copiedProject.id)
        softy.assertThat(createdCopiedProject.name).isEqualTo(copiedProject.name)
    }

    @Test
    @Tag("Negative")
    fun `a user should not be able to copy a non-existent project`() {
        val sourceProjectName = RandomData.getString()
        val copiedProject = TestDataGenerator.generate().project.copy(sourceProject = Locator(sourceProjectName))
        superUserUncheckedRequests
            .getRequest(Endpoint.PROJECTS)
            .create(copiedProject)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body(Matchers.containsString("No project found by name or internal/external id '$sourceProjectName'"))
    }

    @ParameterizedTest
    @Tag("Negative")
    @ValueSource(strings = [""])
    @NullSource
    fun `a user should not be able to copy a project with passing empty or null sourceProject locator`(locator: String?) {
        val copiedProject = TestDataGenerator.generate().project.copy(sourceProject = Locator(locator))
        superUserUncheckedRequests
            .getRequest(Endpoint.PROJECTS)
            .create(copiedProject)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body(Matchers.containsString("No project specified. Either 'id', 'internalId' or 'locator' attribute should be present."))
    }

    @Test
    @Tag("Negative")
    fun `a user cannot create a project with the same id`() {
        val duplicatedProject = TestDataGenerator.generate().project.copy(id = testData.project.id)

        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)
        superUserUncheckedRequests
            .getRequest(Endpoint.PROJECTS)
            .create(duplicatedProject)
            .then()
            .spec(ResponseValidationSpecifications.checkProjectWithIdAlreadyExist(duplicatedProject.id!!))
    }

    @Test
    @Tag("Negative")
    fun `a user cannot create a project with the same name`() {
        val duplicatedProject = TestDataGenerator.generate().project.copy(name = testData.project.name)

        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)
        superUserUncheckedRequests
            .getRequest(Endpoint.PROJECTS)
            .create(duplicatedProject)
            .then()
            .spec(ResponseValidationSpecifications.checkProjectWithNameAlreadyExist(duplicatedProject.name!!))
    }

    @Test
    @Tag("Negative")
    fun `a user cannot create a project with 226 symbols length id`() {
        val invalidProjectId = RandomData.getString(226)
        superUserUncheckedRequests
            .getRequest(Endpoint.PROJECTS)
            .create(testData.project.copy(id = invalidProjectId))
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST) //throws 500: INTERNAL for a whitespace string
            .body(Matchers.containsString("Project ID \"$invalidProjectId\" is invalid:" +
                    " it is ${invalidProjectId.length} characters long while the maximum length is 225"))
    }

    @ParameterizedTest
    @Tag("Negative")
    @NullSource
    @ValueSource(strings = ["", " "])
    fun `a user cannot create a project with an empty or null name`(name: String?) {
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
    @ValueSource(strings = ["", " ", "1", " test", "!@#$", "ололо", "_id"])
    fun `a user cannot create a project with an invalid id`(id: String) {
        superUserUncheckedRequests
            .getRequest(Endpoint.PROJECTS)
            .create(testData.project.copy(id = id))
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST) //actually throws 500: INTERNAL
    }

    @Test
    @Tag("Negative")
    fun `a user cannot create a project without auth`() {
        UncheckedRequests(Specification.unAuthSpec())
            .getRequest(Endpoint.PROJECTS)
            .create(testData.project)
            .then()
            .spec(ResponseValidationSpecifications.checkUnauthorizedError())
    }

    @ParameterizedTest
    @Tag("Negative")
    @MethodSource("usersWithoutProjectCreationRights")
    fun `a user without admin rights cannot create a project`(user: User) {
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(user)

        UncheckedRequests(Specification.authSpec(user))
            .getRequest(Endpoint.PROJECTS)
            .create(testData.project)
            .then()
            .spec(ResponseValidationSpecifications.checkForbiddenError())
    }

    //Advanced homework ↓↓↓

    @Test
    @Tag("Positive")
    fun `a project may be found by name`() {
        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)

        val createdProject = superUserCheckedRequests
            .getRequest<Project>(Endpoint.PROJECTS)
            .search("name:${testData.project.name}")

        softy.assertThat(createdProject).isEqualTo(testData.project)
    }

    @Test
    @Tag("Negative")
    fun `a non-existent project request returns 404 not found`() {
        superUserUncheckedRequests
            .getRequest(Endpoint.PROJECTS)
            .read(RandomData.getString())
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @Test
    @Tag("Negative")
    fun `a project cannot be found by name by an unauthorized user`() {
        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)

        UncheckedRequests(Specification.unAuthSpec())
            .getRequest(Endpoint.PROJECTS)
            .search("name:${testData.project.name}")
            .then()
            .spec(ResponseValidationSpecifications.checkUnauthorizedError())
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
