package ui.pages

import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

abstract class BasePage {

    companion object {
        @JvmStatic
        protected val BASE_WAITING = 30.seconds.toJavaDuration()
    }
}
