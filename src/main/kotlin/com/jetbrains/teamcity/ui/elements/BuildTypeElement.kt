package com.jetbrains.teamcity.ui.elements

import com.codeborne.selenide.SelenideElement

class BuildTypeElement(element: SelenideElement) : BasePageElement(element) {
    val name: SelenideElement = find("span[class*='MiddleEllipsis']")
}
