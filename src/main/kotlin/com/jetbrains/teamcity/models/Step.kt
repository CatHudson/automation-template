package com.jetbrains.teamcity.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Step(
    val id: String = "",
    val name: String = "name",
    val type: String = "simpleRunner",
    val properties: Properties = Properties(),
): BaseModel() {
    companion object {

        val printHelloWorldStep: Step by lazy {
            Step(
                id = "Hello Step",
                name = "hello world",
                type = "simpleRunner",
                properties = Properties(
                    count = 2,
                    property = listOf(
                        Property(
                            name = "script.content",
                            value = "echo Hello, World!"
                        ),
                        Property(
                            name = "use.custom.script",
                            value = "true"
                        )
                    )
                )
            )
        }
    }
}
