package com.jetbrains.teamcity

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

open class BaseTest {

    protected var softy: SoftAssertions = SoftAssertions()

    @BeforeEach
    fun beforeTest() {
        softy = SoftAssertions()
    }

    @AfterEach
    fun afterTest() {
        softy.assertAll()
    }
}
