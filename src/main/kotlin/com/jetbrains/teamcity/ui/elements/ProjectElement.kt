package com.jetbrains.teamcity.ui.elements

import com.codeborne.selenide.SelenideElement

class ProjectElement(element: SelenideElement) : BasePageElement(element) {
    val name: SelenideElement = find("span[class*='MiddleEllipsis']")
    val link: SelenideElement = find("a")
    val button: SelenideElement = find("button")
}
