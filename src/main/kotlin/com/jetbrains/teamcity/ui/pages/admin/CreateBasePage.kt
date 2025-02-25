package com.jetbrains.teamcity.ui.pages.admin

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byName
import com.codeborne.selenide.Selenide.`$`
import com.jetbrains.teamcity.ui.pages.BasePage

abstract class CreateBasePage: BasePage() {

    protected val buildNameInput = `$`("#buildTypeName")
    protected val connectionSuccessfulIndicator = `$`(".connectionSuccessful")
    protected val proceedButtonStepTwo = `$`(byName("createProject"))

    companion object {
        protected const val CREATE_URL = "/admin/createObjectMenu.html?projectId=%s&showMode=%s"

        private val urlInput = `$`("#url")
        private val usernameInput = `$`("#username")
        private val passwordInput = `$`("#password")
        private val baseProceedButton = `$`(byName("createProjectFromUrl"))
    }

    protected fun baseCreateForm(repoUrl: String) {
        urlInput.`val`(repoUrl)
        baseProceedButton.click()
        connectionSuccessfulIndicator.shouldBe(visible, BASE_WAITING)
    }
}
