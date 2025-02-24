package ui.pages

import com.codeborne.selenide.Selenide

class ProjectsPage: BasePage() {

    companion object {

        private const val PROJECTS_URL = "/favorite/projects"

        fun open(): ProjectsPage {
            return Selenide.open(PROJECTS_URL, ProjectsPage::class.java)
        }
    }

    
}
