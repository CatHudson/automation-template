package com.jetbrains.teamcity.ui

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.logevents.SelenideLogger
import com.jetbrains.teamcity.BaseTest
import com.jetbrains.teamcity.api.configuration.Configuration
import com.jetbrains.teamcity.api.enums.Endpoint
import com.jetbrains.teamcity.api.models.User
import com.jetbrains.teamcity.ui.pages.LoginPage
import io.qameta.allure.selenide.AllureSelenide
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit
import com.codeborne.selenide.Configuration as SelenideConfig

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Timeout(value = 3, unit = TimeUnit.MINUTES)
open class BaseUiTest : BaseTest() {

    @BeforeAll
    fun `setup a UI test`() {
        SelenideConfig.browser = Configuration.getProperty("browser").toString()
        SelenideConfig.baseUrl =
            "http://${Configuration.getProperty("host")}:${Configuration.getProperty("port")}"
        SelenideConfig.remote = "http://${Configuration.getProperty("remote")}"
        SelenideConfig.browserSize = Configuration.getProperty("browserSize").toString()
        SelenideConfig.timeout = 10_000

        SelenideConfig.browserCapabilities.setCapability(
            "selenoid:options",
            mapOf(
                "enableVnc" to true,
                "enableLog" to true
            )
        )
        SelenideLogger.addListener(
            "AllureSelenide",
            AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
                .includeSelenideSteps(true)
                .includeSelenideSteps(true)
        )
    }

    @AfterEach
    fun `close WebDriver`() {
        Selenide.closeWebDriver()
    }

    protected fun loginAs(user: User) {
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(user)
        LoginPage.open().login(user)
    }
}
