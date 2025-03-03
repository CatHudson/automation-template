package com.jetbrains.teamcity.ui.pages.setup

import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`
import com.jetbrains.teamcity.api.configuration.Configuration
import com.jetbrains.teamcity.ui.pages.BasePage

class FirstStartPage : BasePage() {

    init {
        restoreButton.shouldBe(visible, LONG_WAITING)
    }

    fun setFirstStart(): FirstStartPage {
        proceedButton.click()
        dbTypeSelector.shouldBe(visible, LONG_WAITING)
        proceedButton.click()
        acceptLicenceCheckbox.should(exist, LONG_WAITING).scrollTo()
        acceptLicenceCheckbox.click()
        submitButton.click()
        usernameInput.shouldBe(visible)
        usernameInput.`val`(Configuration.getProperty("username").toString())
        passwordInput.`val`(Configuration.getProperty("password").toString())
        confirmPasswordInput.`val`(Configuration.getProperty("password").toString())
        submitButton.click()
        return this
    }

    companion object {
        private val proceedButton = `$`("#proceedButton")
        private val restoreButton = `$`("#restoreButton")
        private val dbTypeSelector = `$`("#dbType")
        private val acceptLicenceCheckbox = `$`("#accept")
        private val submitButton = `$`("input[type='submit']")
        private val usernameInput = `$`("#input_teamcityUsername")
        private val passwordInput = `$`("#password1")
        private val confirmPasswordInput = `$`("#retypedPassword")

        fun open(): FirstStartPage {
            return Selenide.open("/", FirstStartPage::class.java)
        }
    }
}
