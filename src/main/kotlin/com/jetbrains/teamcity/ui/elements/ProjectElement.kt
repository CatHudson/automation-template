package com.jetbrains.teamcity.ui.elements

import com.codeborne.selenide.SelenideElement

class ProjectElement(element: SelenideElement) : BasePageElement(element) {
    val name = find("span[class*='MiddleEllipsis']")
}
