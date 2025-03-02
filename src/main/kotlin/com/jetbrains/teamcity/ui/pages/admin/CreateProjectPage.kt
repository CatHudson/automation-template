package com.jetbrains.teamcity.ui.pages.admin

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`

class CreateProjectPage : CreateBasePage() {

    companion object {

        private const val PROJECT_SHOW_MODE = "createProjectMenu"

        private val projectNameInput = `$`("#projectName")
        private val branchInput = `$`("#branch")

        fun open(projectId: String): CreateProjectPage {
            return Selenide.open(
                CREATE_URL.format(projectId, PROJECT_SHOW_MODE),
                CreateProjectPage::class.java
            )
        }
    }

    fun createForm(repoUrl: String): CreateProjectPage {
        baseCreateForm(repoUrl)
        return this
    }

    fun setupProject(projectName: String, buildName: String) {
        projectNameInput.`val`(projectName)
        buildNameInput.`val`(buildName)
        proceedButtonStepTwo.click()
    }
}
