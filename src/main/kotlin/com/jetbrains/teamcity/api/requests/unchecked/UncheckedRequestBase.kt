package com.jetbrains.teamcity.api.requests.unchecked

import com.jetbrains.teamcity.api.enums.Endpoint
import com.jetbrains.teamcity.api.models.BaseModel
import com.jetbrains.teamcity.api.requests.CRUDInterface
import com.jetbrains.teamcity.api.requests.Request
import com.jetbrains.teamcity.api.requests.SearchInterface
import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification

class UncheckedRequestBase(
    private val spec: RequestSpecification,
    private val endpoint: Endpoint
): Request(spec, endpoint), CRUDInterface, SearchInterface {

    override fun create(model: BaseModel): Response {
        return RestAssured
            .given()
            .spec(spec)
            .body(model)
            .post(endpoint.url)
    }

    /**
     * You can pass a parameter name to search by something other than id
     */
    override fun read(id: String): Response {
        return RestAssured
            .given()
            .spec(spec)
            .get("${endpoint.url}/id:$id")
    }

    override fun update(id: String, model: BaseModel): Response {
        return RestAssured
            .given()
            .spec(spec)
            .body(model)
            .put("${endpoint.url}/id:$id")
    }

    override fun delete(id: String): Response {
        return RestAssured
            .given()
            .spec(spec)
            .delete("${endpoint.url}/id:$id")
    }
    
    override fun search(query: String): Response {
        return RestAssured
            .given()
            .spec(spec)
            .get("${endpoint.url}/$query")
    }

    override fun filter(filters: Map<String, String>): Response {
        val locatorValue = filters.entries
            .joinToString(",") { "${it.key}:${it.value}" }

        return RestAssured
            .given()
            .spec(spec)
            .queryParam("locator", locatorValue)
            .get(endpoint.url)

    }
}
