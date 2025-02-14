package com.jetbrains.teamcity.models

data class Step(
    private val id: Int,
    private val name: String,
    private val type: String = "simpleRunner",
)
