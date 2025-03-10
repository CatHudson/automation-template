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
        try {
            val result = block()
            if (result != null) {
                return result
            }
        } catch (e: Throwable) {
            logger.warn("Attempt ${attempt + 1} failed with exception: ${e.message}. Retrying...")
            Thread.sleep(delay)
        }
    }
    throw IllegalStateException("Block failed after $attempts attempts.")
}
