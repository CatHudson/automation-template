package com.jetbrains.teamcity.api.requests

interface SearchInterface {
    /**
     * Returns a list of elements matching the filters. If filters are empty, returns a list of all elements by the given jsonPath.
     */
    fun filter(filters: Map<String, String> = emptyMap(), listJsonPath: String = ""): Any

    /**
     * Returns a single matching element
     */
    fun search(query: String): Any
}
