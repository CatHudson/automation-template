package com.jetbrains.teamcity.ui.pages

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`
import com.jetbrains.teamcity.ui.elements.BuildRunHistoryElement
import io.qameta.allure.Step

class BuildTypePage : BasePage() {

    private val buildRunHistoryBlock = `$`("div[class*='Builds__hasParentGrid']")
    private val buildRunHistoryElements = buildRunHistoryBlock.findAll("div[class*='buildContainer']")
    val buildsInQueueIndicator = buildRunHistoryBlock.find("button[class*='Builds'] span[class='ring-button-content']")
    val title = `$`("h1")

    @Step("Get a list of build runs")
    fun getBuildRuns(): List<BuildRunHistoryElement> {
        if (!buildRunHistoryBlock.`is`(visible))
            Selenide.refresh()
        buildRunHistoryBlock.shouldBe(visible)
        return generatePageElements(buildRunHistoryElements) {
            BuildRunHistoryElement(it)
        }
    }

    init {
        title.shouldBe(visible, BASE_WAITING)
    }

    companion object {

        private const val BUILD_TYPE_BY_NAME_URL = "/buildConfiguration/%s_%s"
        private const val BUILD_TYPE_BY_ID_URL = "/buildConfiguration/%s"

        @Step("Open BuildType page")
        fun open(projectId: String, buildTypeName: String): BuildTypePage {
            return Selenide.open(
                BUILD_TYPE_BY_NAME_URL.format(
                    projectId,
                    buildTypeName.replace("_", "").let {
                        it.replaceFirstChar { char -> char.uppercaseChar() }
                    }
                ),
                BuildTypePage::class.java
            )
        }

        @Step("Open BuildType page")
        fun open(buildTypeId: String): BuildTypePage {
            return Selenide.open(
                BUILD_TYPE_BY_ID_URL.format(buildTypeId),
                BuildTypePage::class.java
            )
        }
    }
}
