package com.jetbrains.teamcity.ui

import com.codeborne.selenide.Condition
import com.jetbrains.teamcity.api.enums.Endpoint
import com.jetbrains.teamcity.ui.pages.ProjectPage
import com.jetbrains.teamcity.ui.pages.ProjectsPage
import org.junit.jupiter.api.Test

class FindProjectByNameTest: BaseUiTest() {

    @Test
    fun `a user should be able to find a project by name`() {
        loginAs(testData.user)

        superUserCheckedRequests.getRequest(Endpoint.PROJECTS).create(testData.project)

        /**
         * I have implemented the searchProjectByName() fun with double click of the Enter key because it seemed like a nice hack, and it works!
         * I understand that a more correct way would be to create ElementsCollection for the elements under the search field
         * However, it seems to me that my hack is a proper demonstration of the YAGNI principle :D
         *
         * What do you think, Alex?
         */
        ProjectsPage.open().searchProjectByName(testData.project.name!!)

        ProjectPage().title.shouldHave(Condition.exactText(testData.project.name!!))
    }
}
