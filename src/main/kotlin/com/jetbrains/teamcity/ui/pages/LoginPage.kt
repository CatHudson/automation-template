package com.jetbrains.teamcity.ui.pages

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`
import com.jetbrains.teamcity.api.models.User
import io.qameta.allure.Step

class LoginPage : BasePage() {

    companion object {

        private const val LOGIN_URL = "/login.html"

        private val usernameInput = `$`("#username")
        private val passwordInput = `$`("#password")
        private val loginButton = `$`(".loginButton")

        @Step("Open login page")
        fun open(): LoginPage {
            return Selenide.open(LOGIN_URL, LoginPage::class.java)
        }
    }

    @Step("Login as {user.username}")
    fun login(user: User): ProjectsPage {
        usernameInput.`val`(user.username)
        passwordInput.`val`(user.password)
        loginButton.click()

        return Selenide.page(ProjectsPage::class.java)
    }
}
