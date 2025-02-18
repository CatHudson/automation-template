package com.jetbrains.teamcity.requests.unchecked

import com.jetbrains.teamcity.enums.Endpoint
import com.jetbrains.teamcity.enums.ReadQueryIdType
import com.jetbrains.teamcity.models.BaseModel
import com.jetbrains.teamcity.requests.CRUDInterface
import com.jetbrains.teamcity.requests.Request
import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification

class UncheckedRequestBase(
    private val spec: RequestSpecification,
    private val endpoint: Endpoint
): Request(spec, endpoint), CRUDInterface {

    override fun create(model: BaseModel): Response {
        return RestAssured
            .given()
            .spec(spec)
            .body(model)
            .post(endpoint.url)
    }

    override fun read(value: String, param: ReadQueryIdType): Response {
        return RestAssured
            .given()
            .spec(spec)
            .get("${endpoint.url}/${param.value.lowercase()}:$value")
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
}
