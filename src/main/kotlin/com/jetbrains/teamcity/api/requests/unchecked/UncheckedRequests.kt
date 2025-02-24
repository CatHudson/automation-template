package com.jetbrains.teamcity.api.requests.unchecked

import com.jetbrains.teamcity.api.enums.Endpoint
import io.restassured.specification.RequestSpecification
import java.util.*

class UncheckedRequests(
    private val specification: RequestSpecification,
) {
    private val requests: EnumMap<Endpoint, UncheckedRequestBase> = EnumMap(Endpoint::class.java)

    init {
        Endpoint.entries.forEach { endpoint ->
            requests[endpoint] = UncheckedRequestBase(specification, endpoint)
        }
    }

    fun getRequest(endpoint: Endpoint): UncheckedRequestBase {
        return requests[endpoint] ?: throw IllegalArgumentException("Cannot find a request for the given endpoint: $endpoint. Check Endpoint class.")
    }
}
