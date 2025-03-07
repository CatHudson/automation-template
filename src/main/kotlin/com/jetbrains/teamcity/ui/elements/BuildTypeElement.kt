package com.jetbrains.teamcity.ui.elements

import com.codeborne.selenide.SelenideElement

class BuildTypeElement(element: SelenideElement) : BasePageElement(element) {
    val name = find("span[class*='MiddleEllipsis']")
    val runBuildButton = find("button[data-test='run-build']")
}
