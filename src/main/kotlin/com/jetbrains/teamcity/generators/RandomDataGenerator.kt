package com.jetbrains.teamcity.generators

import kotlin.math.absoluteValue
import kotlin.random.Random

object RandomData {
    private const val TEST_PREFIX = "test_"
    private const val MAX_LENGTH = 10

    val string: String
        get() = TEST_PREFIX + getString()
    val int: Int
        get() = Random.nextInt()
    val long: Long
        get() = Random.nextLong().absoluteValue

    private fun getString(length: Int = MAX_LENGTH): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
             .map { allowedChars.random() }
            .joinToString("")
    }
}
