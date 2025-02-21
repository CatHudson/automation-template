package com.jetbrains.teamcity.generators

import kotlin.math.absoluteValue
import kotlin.random.Random

object RandomData {
    private const val TEST_PREFIX = "test_"
    private const val MAX_LENGTH = 10

    val int: Int
        get() = Random.nextInt()
    val long: Long
        get() = Random.nextLong().absoluteValue
    val boolean: Boolean
        get() = Random.nextBoolean()

    fun getString(length: Int = MAX_LENGTH): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return TEST_PREFIX + (1..length - TEST_PREFIX.length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun getCyrillicString(length: Int = MAX_LENGTH - TEST_PREFIX.length): String {
        val allowedChars = ('А'..'Я') + ('а'..'я') + ('0'..'9')
        return TEST_PREFIX + (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}
