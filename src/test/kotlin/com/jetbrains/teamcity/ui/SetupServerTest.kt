package com.jetbrains.teamcity.ui

import com.jetbrains.teamcity.ui.pages.setup.FirstStartPage
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("Server-setup")
class SetupServerTest : BaseUiTest() {

    @Test
    fun setupTeamCityServerTest() {
        FirstStartPage.open().setFirstStart()
    }
}
