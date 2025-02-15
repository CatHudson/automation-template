package com.jetbrains.teamcity.generators

import org.apache.commons.lang3.RandomStringUtils
import kotlin.math.max

object RandomData {
    private const val TEST_PREFIX = "test_"
    private const val MAX_LENGTH = 10

    val string: String
        get() = TEST_PREFIX + RandomStringUtils.randomAlphabetic(MAX_LENGTH)

    fun getString(length: Int): String {
        return TEST_PREFIX + RandomStringUtils
            .randomAlphabetic(max((length - TEST_PREFIX.length).toDouble(), MAX_LENGTH.toDouble()).toInt())
    }
}
