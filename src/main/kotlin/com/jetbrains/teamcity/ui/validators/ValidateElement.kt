package com.jetbrains.teamcity.ui.validators

import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.SelenideElement

object ValidateElement {

    fun byText(element: SelenideElement, expectedText: String) {
        element.shouldBe(visible)
        element.shouldHave(exactText(expectedText))
    }
}
