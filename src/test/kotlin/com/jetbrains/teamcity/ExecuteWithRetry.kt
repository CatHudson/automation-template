package com.jetbrains.teamcity

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <T> executeWithRetry(
    attempts: Int = 5,
    delay: Long = 250,
    block: () -> T
): T {
    val logger: Logger = LoggerFactory.getLogger("RetryBlock")
    repeat(attempts) { attempt ->
        val result = block()
        if (result != null) {
            return result
        } else {
            logger.warn("Attempt ${attempt + 1} failed. Retrying...")
            Thread.sleep(delay)
        }
    }
    throw IllegalStateException("$block fun failed after $attempts attempts.")
}
