package com.jetbrains.teamcity.ui.elements

import com.codeborne.selenide.SelenideElement

class BuildRunHistoryElement(element: SelenideElement) : BasePageElement(element) {
    val buildNumber = find("div[class*='Build__number'] span[class*='MiddleEllipsis']")
    val buildStatus = find("div[class*='Build__status'] span[class*='MiddleEllipsis']")
}
