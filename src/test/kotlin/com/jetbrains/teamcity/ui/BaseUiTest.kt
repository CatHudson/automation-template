package com.jetbrains.teamcity.ui

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Configuration as SelenideConfig
import com.jetbrains.teamcity.BaseTest
import com.jetbrains.teamcity.api.configuration.Configuration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseUiTest: BaseTest() {

    @BeforeAll
    fun `setup a UI test`() {
        SelenideConfig.browser = Configuration.getProperty("browser").toString()
        SelenideConfig.baseUrl = "http://${Configuration.getProperty("host").toString()}:${Configuration.getProperty("port").toString()}"
        SelenideConfig.remote = "http://${Configuration.getProperty("remote").toString()}"
        SelenideConfig.browserSize = Configuration.getProperty("browserSize").toString()

        SelenideConfig.browserCapabilities.setCapability(
            "selenoid:options",
            mapOf(
                "enableVnc" to true,
                "enableLog" to true,
            )
        )
    }

    @AfterEach
    fun `close WebDriver`() {
        Selenide.closeWebDriver()
    }
}
