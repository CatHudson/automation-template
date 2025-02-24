package com.jetbrains.teamcity.api.requests.checked

import com.jetbrains.teamcity.api.enums.Endpoint
import com.jetbrains.teamcity.api.models.BaseModel
import io.restassured.specification.RequestSpecification
import java.util.*

class CheckedRequests(
    private val specification: RequestSpecification,
) {
    private val requests: EnumMap<Endpoint, CheckedRequestBase<out BaseModel>> = EnumMap(Endpoint::class.java)

    init {
        Endpoint.entries.forEach { endpoint ->
            requests[endpoint] = CheckedRequestBase(specification, endpoint)
        }
    }

    fun getRequest(endpoint: Endpoint): CheckedRequestBase<out BaseModel> {
        return requests[endpoint]
            ?: throw IllegalArgumentException("Cannot find a request for the given endpoint: $endpoint. Check Endpoint class.")
    }

    internal inline fun <reified T : BaseModel> getRequest(endpoint: Endpoint): CheckedRequestBase<T> {
        @Suppress("unchecked_cast") return requests[endpoint] as CheckedRequestBase<T>?
            ?: throw IllegalArgumentException("Cannot find a request for the given endpoint: $endpoint. Check Endpoint class.")
    }
}
