package com.jetbrains.teamcity

import com.jetbrains.teamcity.requests.checked.CheckedRequests
import com.jetbrains.teamcity.spec.Specification
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

open class BaseTest {

    protected var softy: SoftAssertions = SoftAssertions()
    protected var superUserCheckedRequests = CheckedRequests(Specification.superUserSpec())

    @BeforeEach
    fun beforeTest() {
        softy = SoftAssertions()
    }

    @AfterEach
    fun afterTest() {
        softy.assertAll()
    }
}
