package com.jetbrains.teamcity.ui

import com.jetbrains.teamcity.api.enums.Endpoint
import com.jetbrains.teamcity.api.extensions.step
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import ui.pages.LoginPage

@Tag("Regression")
class CreateProjectTest: BaseUiTest() {

    @Test
    @Tag("Positive")
    fun `a user should be able to create a project`() {
        //setup
        step("Login as user") { }
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(testData.user)
        LoginPage.open().login(testData.user)

        //action on the UI
        step("Open `create project page` http://host.docker.internal:8111/admin/createObjectMenu.html") { }
        step("Send all project parameters (repo URL)") {}
        step("Click `proceed`") {}
        step("Fix project and build type names") {}
        step("Click `proceed`") {}

        //check on the API level
        step("Check that Project and BuildType were created on the API level") {}

        //check the processed data displayed on the UI
        step("Check that project is visible on Projects Page") {}
    }

    @Test
    @Tag("Negative")
    fun `a user should not be able to create a project without a name`() {
        //setup
        step("Login as user") { }
        step("Fix the amount of existing projects") { }

        //action on the UI
        step("Open `create project page` http://host.docker.internal:8111/admin/createObjectMenu.html") { }
        step("Send all project parameters (repo URL)") {}
        step("Click `proceed`") {}
        step("Set project name as an empty string") {}
        step("Click `proceed`") {}

        //check on the API level
        step("Check that the amount of existing projects did not change") { }

        //check the processed data displayed on the UI
        step("Check error on the UI level") {}
    }
}