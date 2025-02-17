package com.jetbrains.teamcity

import com.jetbrains.teamcity.generators.TestDataGenerator
import com.jetbrains.teamcity.generators.TestDataStorage
import com.jetbrains.teamcity.requests.checked.CheckedRequests
import com.jetbrains.teamcity.requests.unchecked.UncheckedRequests
import com.jetbrains.teamcity.spec.Specification
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

open class BaseTest {

    protected var softy: SoftAssertions = SoftAssertions()
    protected var superUserCheckedRequests = CheckedRequests(Specification.superUserSpec())
    protected var superUserUncheckedRequests = UncheckedRequests(Specification.superUserSpec())
    protected var testData = TestDataGenerator.generate()

    @BeforeEach
    fun beforeTest() {
        testData = TestDataGenerator.generate()
        softy = SoftAssertions()
    }

    @AfterEach
    fun afterTest() {
        softy.assertAll()
        TestDataStorage.deleteEntities()
    }
}
