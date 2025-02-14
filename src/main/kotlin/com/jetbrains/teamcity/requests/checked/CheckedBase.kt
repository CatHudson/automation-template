package com.jetbrains.teamcity.requests.checked

import com.jetbrains.teamcity.enums.Endpoint
import com.jetbrains.teamcity.models.BaseModel
import com.jetbrains.teamcity.requests.CRUDInterface
import com.jetbrains.teamcity.requests.Request
import com.jetbrains.teamcity.requests.unchecked.UncheckedBase
import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import org.apache.http.HttpStatus
import kotlin.reflect.KClass

@Suppress("unchecked_cast")
class CheckedBase<T : BaseModel>(
    private val spec: RequestSpecification,
    private val endpoint: Endpoint,
) : Request(spec, endpoint), CRUDInterface {

    private val uncheckedBase = UncheckedBase(spec, endpoint)

    override fun create(model: BaseModel): T {
        return uncheckedBase
            .create(model)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract().`as`(endpoint.modelClass) as T
    }

    override fun read(id: Int): T {
        return uncheckedBase
            .read(id)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract().`as`(endpoint.modelClass) as T
    }

    override fun update(id: Int, model: BaseModel): T {
        return uncheckedBase
            .update(id, model)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract().`as`(endpoint.modelClass) as T
    }

    override fun delete(id: Int): Any {
        return uncheckedBase
            .delete(id)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
    }
}
