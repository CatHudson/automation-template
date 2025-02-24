package com.jetbrains.teamcity.ui.elements

import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.SelenideElement
import org.openqa.selenium.By
import com.jetbrains.teamcity.ui.pages.BasePage

abstract class BasePageElement(
    open val element: SelenideElement
): BasePage() {

    protected fun find(by: By): SelenideElement {
        return element.`$`(by)
    }

    protected fun find(cssSelector: String): SelenideElement {
        return element.`$`(cssSelector)
    }

    protected fun findAll(by: By): ElementsCollection {
        return element.`$$`(by)
    }

    protected fun findAll(cssSelector: String): ElementsCollection {
        return element.`$$`(cssSelector)
    }
}
