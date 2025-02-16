package com.jetbrains.teamcity.extensions

import io.qameta.allure.Allure

/**
 * An extension function to solve Allure step overload ambiguity
 */
@Throws(Throwable::class)
fun step(title: String, action: () -> Any) = Allure.step(title, Allure.ThrowableRunnableVoid { action() })

/**
 * An extension function to solve Allure step overload ambiguity
 */
@Throws(Throwable::class)
fun <T> step(title: String, action: () -> T): T = Allure.step(title, Allure.ThrowableRunnable { action() })
