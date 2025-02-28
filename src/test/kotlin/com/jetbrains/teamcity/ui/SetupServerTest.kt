package com.jetbrains.teamcity.ui

import com.jetbrains.teamcity.ui.pages.setup.FirstStartPage
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class SetupServerTest : BaseUiTest() {

    @Test
    @Tag("Setup")
    fun setupTeamCityServerTest() {
        FirstStartPage.open().setFirstStart()
    }
}
