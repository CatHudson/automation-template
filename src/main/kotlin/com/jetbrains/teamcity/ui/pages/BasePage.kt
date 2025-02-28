package com.jetbrains.teamcity.ui.pages

import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.SelenideElement
import com.jetbrains.teamcity.ui.elements.BasePageElement
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

abstract class BasePage {

    companion object {
        @JvmStatic
        protected val BASE_WAITING = 30.seconds.toJavaDuration()
        @JvmStatic
        protected val LONG_WAITING = 3.minutes.toJavaDuration()
    }

    protected fun <T: BasePageElement> generatePageElements(
        collection: ElementsCollection,
        creator: (SelenideElement) -> T
    ): List<T> {
        return collection.map { creator(it) }
    }
}
