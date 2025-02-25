package com.jetbrains.teamcity.ui.pages.admin

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`

class CreateBuildTypePage : CreateBasePage() {

    companion object {

        private const val BUILD_TYPE_SHOW_MODE = "createBuildTypeMenu"

        fun open(projectId: String): CreateBuildTypePage {
            return Selenide.open(
                CREATE_URL.format(projectId, BUILD_TYPE_SHOW_MODE),
                CreateBuildTypePage::class.java
            )
        }
    }

    val branchInput = `$`("#branch")

    fun createForm(repoUrl: String): CreateBuildTypePage {
        baseCreateForm(repoUrl)
        return this
    }

    fun setupBuildType(buildName: String) {
        buildNameInput.`val`(buildName)
        proceedButtonStepTwo.click()
    }
}
