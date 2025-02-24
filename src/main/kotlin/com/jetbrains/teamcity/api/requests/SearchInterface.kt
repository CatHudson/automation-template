package com.jetbrains.teamcity.api.requests

interface SearchInterface {
    fun filter(filters: Map<String, String>): Any
    fun search(query: String): Any
}
